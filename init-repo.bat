@echo off

git remote |findstr upstream > NUL
if not errorlevel 1 (
  echo init-repo.sh was already done on this repository.
  exit /b 1
)

git status --untracked-files=no --porcelain |findstr . > NUL
if not errorlevel 1 (
  echo init-repo.sh is intended to work on a freshly created repository. The working copy of this reposity is dirty. Please checkout or stash changes and run ./init-repo.sh again.
  exit /b 1
)

git remote add upstream https://github.com/YuMSh-Ivanov/queue-2-task.git
git fetch upstream
git branch feedback upstream/master
git push -u origin feedback:feedback
git checkout feedback
git branch -f master feedback
git checkout master
git push -f origin master
