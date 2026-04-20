/**
 * beforeShellExecution: runs before `git push`.
 * Same quality bar as .cursor/hooks.json afterFileEdit (java compile, front lint, node syntax).
 */
'use strict';

const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');

function readHookInput() {
  try {
    const raw = fs.readFileSync(0, 'utf8');
    if (!raw || !String(raw).trim()) {
      return {};
    }
    return JSON.parse(raw);
  } catch {
    return {};
  }
}

const input = readHookInput();
const command = typeof input.command === 'string' ? input.command : '';

function respond(obj) {
  console.log(JSON.stringify(obj));
  process.exit(0);
}

if (!/\bgit\s+push\b/.test(command)) {
  respond({ permission: 'allow' });
}

const root = path.resolve(__dirname, '..', '..');
const steps = [
  { label: 'java-server: mvn clean compile', cmd: 'cd java-server && mvn clean compile' },
  { label: 'front: pnpm run lint', cmd: 'cd front && pnpm run lint' },
  { label: 'node-server: node --check index.js', cmd: 'cd node-server && node --check index.js' },
];

for (const step of steps) {
  try {
    execSync(step.cmd, { cwd: root, stdio: 'inherit', shell: true });
  } catch {
    respond({
      permission: 'deny',
      user_message: `Push 前自动审查未通过：${step.label}`,
      agent_message: `pre-push 审查失败（${step.label}）。请先修复错误后再执行 git push。`,
    });
  }
}

respond({ permission: 'allow' });
