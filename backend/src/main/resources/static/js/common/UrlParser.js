export function getResourceId(url) {
  const pathname = url.pathname;
  const parts = pathname.split("/");
  return parts[parts.length - 1];
}
