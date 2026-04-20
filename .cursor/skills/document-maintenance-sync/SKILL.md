---
name: document-maintenance-sync
description: >-
  Maintain and synchronize formal project docs with temporary markdown notes
  before merge. Detect root-level temporary .md files, transcribe useful
  content into official docs, then clean up redundant temp files safely.
  Use when the user asks to sync notes into README/interface docs, organize docs
  before merge, or clean temporary markdown files.
---

# Document Maintenance & Synchronization

Use this workflow to keep project documentation accurate and merge-ready.

## 1) Core goal

Standardize handling of both:
- Formal project docs
- Temporary development markdown notes

Before code is merged into `main`, ensure implementation logic, interface changes, and design notes are migrated from temporary files into formal docs, then remove redundant temp markdown files.

## 2) Required doc map

Always recognize these canonical documents:

- Root formal doc: `./README.md`
- Root exam doc: `./exam.md`
- Frontend doc: `front/README.md`
- Java backend doc: `java-server/接口.md`
- Node backend doc: `node-server/README.md`

Temporary markdown pool:
- Any root-level `*.md` file except the five canonical docs above
- Example: `analysis.md`, `dev_notes.md`, `tmp_design.md`

## 3) Mandatory workflow

### Phase A: Read before action

Never invent doc content.

1. Scan root temporary markdown files first.
2. Fully read both:
   - target formal doc(s), and
   - source temporary markdown file(s)
3. Analyze content mapping before editing.

### Phase B: Transcription & merge

Route content by category:

- API/schema/protocol changes -> `java-server/接口.md`
- Frontend component/build/run notes -> `front/README.md`
- Middleware/Node logic/BFF config -> `node-server/README.md`
- Global architecture/env/project overview -> `./README.md`

Merge rules:

- Preserve existing formal doc structure.
- Remove duplication and resolve conflicts.
- Keep Markdown clean (heading hierarchy, fenced code blocks, readable sections).

### Phase C: Cleanup

After successful transcription:

1. Run `git status` and verify formal docs are modified as expected.
2. Delete root-level temporary markdown files already migrated.

Safety rules:

- Never delete any `README.md` in any directory.
- Never delete canonical docs listed in section 2.

## 4) Trigger phrases

Activate this skill when user intent matches:

- "Organize docs before merge."
- "Sync my dev notes into README."
- "Clean temporary markdown files."
- "Finish this phase and synchronize docs."

## 5) User-facing progress messages

During execution, explicitly report what is being migrated and cleaned.

Example:

"Detected temporary file `dev_analysis.md`; migrating API changes into `java-server/接口.md`. After verification, I will remove the original temp file."

