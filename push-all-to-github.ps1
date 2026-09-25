<#
  RAILCONNECT — Automated Multi-Repository GitHub Push Utility
  Usage:
    .\push-all-to-github.ps1 -GitHubUsername "your_github_username"
#>
param (
    [Parameter(Mandatory=$true)]
    [string]$GitHubUsername
)

$repos = @(
  "railconnect-frontend",
  "railconnect-backend",
  "railconnect-database",
  "railconnect-auth",
  "railconnect-booking",
  "railconnect-seat-engine",
  "railconnect-weather",
  "railconnect-berth-exchange",
  "railconnect-admin",
  "railconnect-docs"
)

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "  🚆 RAILCONNECT — Pushing 10 Repositories to GitHub" -ForegroundColor Green
Write-Host "  Target Account: $GitHubUsername" -ForegroundColor Yellow
Write-Host "==========================================================" -ForegroundColor Cyan

foreach ($repo in $repos) {
    $repoPath = Join-Path $PSScriptRoot $repo
    if (Test-Path $repoPath) {
        Write-Host "`n--> Processing: $repo" -ForegroundColor Cyan
        $remoteUrl = "https://github.com/$GitHubUsername/$repo.git"
        
        # Check if remote origin already exists
        $existingRemote = git -C $repoPath remote get-url origin 2>$null
        if ($null -eq $existingRemote) {
            git -C $repoPath remote add origin $remoteUrl
            Write-Host "    Added remote origin: $remoteUrl" -ForegroundColor Gray
        } else {
            git -C $repoPath remote set-url origin $remoteUrl
            Write-Host "    Updated remote origin: $remoteUrl" -ForegroundColor Gray
        }
        
        Write-Host "    Pushing branch 'main' to GitHub..." -ForegroundColor Yellow
        git -C $repoPath push -u origin main
        if ($LASTEXITCODE -eq 0) {
            Write-Host "    ✓ $repo pushed successfully!" -ForegroundColor Green
        } else {
            Write-Host "    ⚠️ Could not push $repo. Ensure you have created the empty repository on GitHub first." -ForegroundColor Red
        }
    }
}

Write-Host "`n==========================================================" -ForegroundColor Cyan
Write-Host "  Finished processing all 10 repositories." -ForegroundColor Green
Write-Host "==========================================================" -ForegroundColor Cyan
