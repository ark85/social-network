local log = require('log')

local applicationUsername = os.getenv('TARANTOOL_USERNAME')
local applicationPassword = os.getenv('TARANTOOL_PASSWORD')

if applicationUsername == nil or applicationUsername == ''
        or applicationPassword == nil or applicationPassword == '' then
    error('TARANTOOL_USERNAME and TARANTOOL_PASSWORD are required')
end

box.cfg({
    listen = '0.0.0.0:3301',
    memtx_dir = '/var/lib/tarantool',
    wal_dir = '/var/lib/tarantool',
})

box.schema.user.create(applicationUsername, {
    password = applicationPassword,
    if_not_exists = true,
})

dofile('/opt/tarantool/schema.lua')
dofile('/opt/tarantool/import_dialogs.lua')

local console = require('console')
local consoleSocket = '/var/lib/tarantool/console.sock'
os.remove(consoleSocket)
console.listen(consoleSocket)

log.info('tarantool is ready for ' .. applicationUsername)
