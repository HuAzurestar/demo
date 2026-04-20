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

/** 占比 = main_listed_capital / turnover * 100，保留两位小数；非法为 null */
function computeRatioPct(mainListedCapital, turnover) {
  if (mainListedCapital == null || turnover == null) return null;
  if (!Number.isFinite(mainListedCapital) || !Number.isFinite(turnover)) return null;
  if (turnover === 0) return null;
  const raw = (mainListedCapital / turnover) * 100;
  if (!Number.isFinite(raw)) return null;
  return Math.round(raw * 100) / 100;
}

function enrichRatioAndSortStockList(payload) {
  const list = payload?.data?.stock_list;
  if (!Array.isArray(list)) return;
  for (const row of list) {
    row.ratio_pct = computeRatioPct(row.main_listed_capital, row.turnover);
  }
  list.sort((a, b) => {
    const ta = a.turnover;
    const tb = b.turnover;
    if (ta == null || !Number.isFinite(ta)) return 1;
    if (tb == null || !Number.isFinite(tb)) return -1;
    return tb - ta;
  });
}

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
        const payload = JSON.parse(content);
        enrichRatioAndSortStockList(payload);
        sendJson(res, 200, payload);
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
