local applicationUsername = os.getenv('TARANTOOL_USERNAME')

local dialogMessages = box.schema.space.create('dialog_messages', { if_not_exists = true })
dialogMessages:format({
    { name = 'id', type = 'uuid' },
    { name = 'dialog_id', type = 'uuid' },
    { name = 'from_user_id', type = 'uuid' },
    { name = 'to_user_id', type = 'uuid' },
    { name = 'text', type = 'string' },
    { name = 'created_at', type = 'datetime' },
})
dialogMessages:create_index('primary', {
    parts = { 'id' },
    if_not_exists = true,
})
dialogMessages:create_index('dialog_created', {
    parts = { 'dialog_id', 'created_at', 'id' },
    unique = true,
    if_not_exists = true,
})

local function replaceStoredFunction(functionName, functionBody)
    if box.func[functionName] ~= nil then
        box.schema.func.drop(functionName)
    end
    box.schema.func.create(functionName, {
        language = 'LUA',
        body = functionBody,
        setuid = true,
    })
    box.schema.user.grant(applicationUsername, 'execute', 'function', functionName, { if_not_exists = true })
end

replaceStoredFunction('dialog_send', [[
function(dialog_id, from_user_id, to_user_id, text, created_at)
    local message_id = require('uuid').new()
    box.space.dialog_messages:insert({
        message_id, dialog_id, from_user_id, to_user_id, text, created_at
    })
    return message_id
end
]])

replaceStoredFunction('dialog_list', [[
function(dialog_id)
    local messages = {}
    for _, message in box.space.dialog_messages.index.dialog_created:pairs(
        { dialog_id }, { iterator = 'REQ' }
    ) do
        if message.dialog_id ~= dialog_id then
            break
        end
        table.insert(messages, {
            id = message.id,
            dialogId = message.dialog_id,
            fromUserId = message.from_user_id,
            toUserId = message.to_user_id,
            text = message.text,
            creationDateTime = message.created_at,
        })
    end
    return unpack(messages)
end
]])
