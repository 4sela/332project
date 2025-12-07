# Create output directories
Write-Host "Creating output directories..." -ForegroundColor Cyan
for ($i = 1; $i -le 5; $i++) {
    New-Item -ItemType Directory -Path "output$i" -Force | Out-Null
}
Write-Host "Created 5 output directories" -ForegroundColor Green

# Start Master in background
Write-Host "Starting Master server..." -ForegroundColor Cyan
Start-Process powershell -ArgumentList "-NoExit", "-Command", "sbt 'runMain Master.UserInterface 5'"

# Wait for Master to start
Write-Host "Waiting for Master to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 5

# Start 5 workers
Write-Host "Starting 5 workers..." -ForegroundColor Cyan
for ($i = 1; $i -le 5; $i++) {
    $cmd = "sbt 'runMain Worker.UserInterface localhost:30040 -I input$i -O output$i'"
    Start-Process powershell -ArgumentList "-NoExit", "-Command", $cmd
    Write-Host "  Started Worker $i" -ForegroundColor Gray
    Start-Sleep -Seconds 2  # Stagger worker starts
}

Write-Host "`n All workers started!" -ForegroundColor Green
Write-Host "Watch the Master window for progress..." -ForegroundColor Yellow
