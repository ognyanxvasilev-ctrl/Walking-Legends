const OFFLINE_NOTICE = "You’re offline. Your saved progress and discovery cards still work; map tiles need a connection.";

export function getConnectionStatus(isOnline) {
  return isOnline === false
    ? { isOnline: false, notice: OFFLINE_NOTICE }
    : { isOnline: true, notice: "" };
}
