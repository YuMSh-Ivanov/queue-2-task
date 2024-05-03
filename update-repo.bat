@echo off

git status --porcelain --untracked-files=no |findstr . > NUL
if not errorlevel 1 (
  echo In order to update the repository working directory must be clean. Please commit, checkout or stash changes and run ./update-repo.sh again.
  exit /b 1
)

git checkout feedback
git pull --ff-only upstream master
git push origin
git checkout master
git merge feedback
git push origin
