local memberTryCount = redis.call("INCR", KEYS[1])
redis.call("EXPIRE", KEYS[1], 1)
if memberTryCount > tonumber(ARGV[1])
then
  return -2
end
local remainAmount = redis.call("DECR", KEYS[2])
if remainAmount < 0
then
  return -1
end
return remainAmount

