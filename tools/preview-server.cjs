const http = require("node:http");
const { readFile } = require("node:fs");
const { resolve, extname, sep } = require("node:path");

const root = resolve(process.cwd());
const port = Number(process.env.PORT || 4173);
const mimeTypes = {
  ".html": "text/html; charset=utf-8",
  ".js": "text/javascript; charset=utf-8",
  ".json": "application/json; charset=utf-8",
  ".css": "text/css; charset=utf-8",
};

http
  .createServer((request, response) => {
    const pathname = decodeURIComponent(new URL(request.url, "http://localhost").pathname);
    const file = resolve(root, `.${pathname === "/" ? "/index.html" : pathname}`);

    if (!file.startsWith(`${root}${sep}`)) {
      response.writeHead(403).end("Forbidden");
      return;
    }

    readFile(file, (error, contents) => {
      if (error) {
        response.writeHead(error.code === "ENOENT" ? 404 : 500).end(error.code === "ENOENT" ? "Not found" : "Server error");
        return;
      }
      response.writeHead(200, {
        "Content-Type": mimeTypes[extname(file)] || "application/octet-stream",
        "Cache-Control": "no-store",
      });
      response.end(contents);
    });
  })
  .listen(port, "127.0.0.1", () => {
    console.log(`Walking Legends preview: http://127.0.0.1:${port}`);
  });
