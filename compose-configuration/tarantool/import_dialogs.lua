local log = require('log')

local dialogMessageColumns = [[
    SELECT id,
           dialog_id,
           from_user_id,
           to_user_id,
           text,
           to_char(creation_date_time, 'YYYY-MM-DD"T"HH24:MI:SS.US"Z"') AS creation_date_time
    FROM dialog_messages
]]

local function environment(name)
    local value = os.getenv(name)
    if value == nil or value == '' then
        error(name .. ' is required')
    end
    return value
end

function import_dialog_messages(batchSize)
    if batchSize == nil then
        batchSize = 1000
    end

    local pg = require('pg')
    local uuid = require('uuid')
    local datetime = require('datetime')
    local connection = pg.connect({
        host = 'citus-coordinator',
        port = 5432,
        user = environment('SOCIAL_NETWORK_DB_USERNAME'),
        password = environment('SOCIAL_NETWORK_DB_PASSWORD'),
        db = environment('SOCIAL_NETWORK_DB_NAME'),
    })

    local importedMessageCount = 0
    local lastDialogId
    local lastMessageId
    local rows = connection:execute(
        dialogMessageColumns .. ' ORDER BY dialog_id, id LIMIT $1::bigint',
        batchSize
    )[1]
    while rows ~= nil and #rows > 0 do
        for _, row in ipairs(rows) do
            if row.creation_date_time == nil then
                connection:close()
                error('dialog message ' .. row.id .. ' has no creation_date_time')
            end
            local text = row.text
            if text == nil then
                text = box.NULL
            end
            box.space.dialog_messages:replace({
                uuid.fromstr(row.id),
                uuid.fromstr(row.dialog_id),
                uuid.fromstr(row.from_user_id),
                uuid.fromstr(row.to_user_id),
                text,
                datetime.parse(row.creation_date_time),
            })
            lastDialogId = row.dialog_id
            lastMessageId = row.id
            importedMessageCount = importedMessageCount + 1
        end
        log.info('imported dialog messages: ' .. importedMessageCount)
        rows = connection:execute(
            dialogMessageColumns .. [[ WHERE (dialog_id, id) > ($1::uuid, $2::uuid)
                ORDER BY dialog_id, id
                LIMIT $3::bigint]],
            lastDialogId,
            lastMessageId,
            batchSize
        )[1]
    end

    local citusCount = tonumber(connection:execute(
        'SELECT count(*)::text AS dialog_message_count FROM dialog_messages'
    )[1][1].dialog_message_count)
    connection:close()
    local tarantoolCount = tonumber(box.space.dialog_messages:len())
    if citusCount == nil or tarantoolCount == nil or tarantoolCount < citusCount then
        error('Tarantool has ' .. tostring(tarantoolCount) .. ' dialog messages, Citus has ' .. tostring(citusCount))
    end
    log.info(string.format(
        'dialog import finished, imported=%d, citus=%d, tarantool=%d',
        importedMessageCount,
        citusCount,
        tarantoolCount
    ))
    return importedMessageCount, citusCount, tarantoolCount
end
