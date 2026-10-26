#!/bin/sh

unset TT_APP_NAME
unset TT_INSTANCE_NAME
unset TT_CONFIG

exec tarantool "$@"
