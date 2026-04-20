const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = process.env.PORT || 80;
const DATA_FILE = path.join(__dirname, 'data.json');

const sendJson = (res, status, payload) => {
  res.writeHead(status, {
    'Content-Type': 'application/json; charset=utf-8',
    'Access-Control-Allow-Origin': '*',
  });
  res.end(JSON.stringify(payload));
};

const server = http.createServer((req, res) => {
  const pathname = new URL(req.url, `http://${req.headers.host}`).pathname;

  if (req.method === 'GET' && pathname === '/indicator/capital/v1/grey_rank') {
    fs.readFile(DATA_FILE, 'utf-8', (err, content) => {
      if (err) {
        console.error('[server] read data.json failed:', err);
        sendJson(res, 500, { status_code: -1, status_msg: 'read data.json failed' });
        return;
      }
      try {
        sendJson(res, 200, JSON.parse(content));
      } catch (e) {
        console.error('[server] parse data.json failed:', e);
        sendJson(res, 500, { status_code: -1, status_msg: 'invalid data.json' });
      }
    });
    return;
  }

  sendJson(res, 404, { status_code: -1, status_msg: 'Not Found' });
});

server.listen(PORT, () => {
  console.log(`Server is running at http://localhost:${PORT}`);
  console.log(`  GET http://localhost:${PORT}/indicator/capital/v1/grey_rank`);
});
