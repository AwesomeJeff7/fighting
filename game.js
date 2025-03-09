class Fighter {
    constructor(x, y, color, isPlayer1) {
        this.x = x;
        this.y = y;
        this.width = 50;
        this.height = 100;
        this.color = color;
        this.velocityY = 0;
        this.velocityX = 0;
        this.isOnGround = false;
        this.health = 100;
        this.isAttacking = false;
        this.attackBox = {
            width: 70,
            height: 50,
            offsetX: isPlayer1 ? 50 : -70
        };
        this.isPlayer1 = isPlayer1;
    }

    update() {
        // Apply gravity
        this.velocityY += 0.8;
        this.y += this.velocityY;
        this.x += this.velocityX;

        // Ground collision
        if (this.y + this.height > canvas.height - 20) {
            this.y = canvas.height - this.height - 20;
            this.velocityY = 0;
            this.isOnGround = true;
        } else {
            this.isOnGround = false;
        }

        // Wall collision
        if (this.x < 0) this.x = 0;
        if (this.x + this.width > canvas.width) this.x = canvas.width - this.width;

        // Friction
        this.velocityX *= 0.9;
    }

    draw(ctx) {
        // Draw character
        ctx.fillStyle = this.color;
        ctx.fillRect(this.x, this.y, this.width, this.height);

        // Draw attack box when attacking
        if (this.isAttacking) {
            ctx.fillStyle = 'rgba(255, 0, 0, 0.5)';
            ctx.fillRect(
                this.x + this.attackBox.offsetX,
                this.y + 25,
                this.attackBox.width,
                this.attackBox.height
            );
        }
    }

    attack() {
        if (!this.isAttacking) {
            this.isAttacking = true;
            setTimeout(() => {
                this.isAttacking = false;
            }, 100);
        }
    }
}

// Canvas setup
const canvas = document.getElementById('gameCanvas');
const ctx = canvas.getContext('2d');
canvas.width = 800;
canvas.height = 400;

// Create players
const player1 = new Fighter(100, 200, '#54E44C', true);
const player2 = new Fighter(650, 200, '#e74c3c', false);

// Game controls
const keys = {
    a: { pressed: false },
    d: { pressed: false },
    ArrowLeft: { pressed: false },
    ArrowRight: { pressed: false }
};

// Event listeners
window.addEventListener('keydown', (event) => {
    switch (event.key) {
        // Player 1 controls
        case 'a':
            keys.a.pressed = true;
            break;
        case 'd':
            keys.d.pressed = true;
            break;
        case 'w':
            if (player1.isOnGround) {
                player1.velocityY = -15;
            }
            break;
        case ' ':
            player1.attack();
            break;

        // Player 2 controls
        case 'ArrowLeft':
            keys.ArrowLeft.pressed = true;
            break;
        case 'ArrowRight':
            keys.ArrowRight.pressed = true;
            break;
        case 'ArrowUp':
            if (player2.isOnGround) {
                player2.velocityY = -15;
            }
            break;
        case 'Enter':
            player2.attack();
            break;
    }
});

window.addEventListener('keyup', (event) => {
    switch (event.key) {
        case 'a':
            keys.a.pressed = false;
            break;
        case 'd':
            keys.d.pressed = false;
            break;
        case 'ArrowLeft':
            keys.ArrowLeft.pressed = false;
            break;
        case 'ArrowRight':
            keys.ArrowRight.pressed = false;
            break;
    }
});

// Collision detection
function checkCollision(attacker, defender) {
    if (
        attacker.isAttacking &&
        attacker.x + (attacker.isPlayer1 ? attacker.attackBox.offsetX : 0) < defender.x + defender.width &&
        attacker.x + (attacker.isPlayer1 ? attacker.attackBox.offsetX : 0) + attacker.attackBox.width > defender.x &&
        attacker.y + 25 < defender.y + defender.height &&
        attacker.y + 25 + attacker.attackBox.height > defender.y
    ) {
        attacker.isAttacking = false;
        defender.health -= 10;
        updateHealthBars();
    }
}

// Update health bars
function updateHealthBars() {
    document.getElementById('p1Health').style.width = `${player1.health}%`;
    document.getElementById('p2Health').style.width = `${player2.health}%`;
}

// Game loop
function gameLoop() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // Draw ground
    ctx.fillStyle = '#2ecc71';
    ctx.fillRect(0, canvas.height - 20, canvas.width, 20);

    // Update player positions based on controls
    if (keys.a.pressed) player1.velocityX = -5;
    if (keys.d.pressed) player1.velocityX = 5;
    if (keys.ArrowLeft.pressed) player2.velocityX = -5;
    if (keys.ArrowRight.pressed) player2.velocityX = 5;

    // Update and draw players
    player1.update();
    player2.update();
    player1.draw(ctx);
    player2.draw(ctx);

    // Check for attacks
    checkCollision(player1, player2);
    checkCollision(player2, player1);

    // Check for game over
    if (player1.health <= 0 || player2.health <= 0) {
        ctx.fillStyle = 'rgba(0, 0, 0, 0.7)';
        ctx.fillRect(0, 0, canvas.width, canvas.height);
        ctx.fillStyle = 'white';
        ctx.font = '48px Arial';
        ctx.textAlign = 'center';
        ctx.fillText(
            `${player1.health <= 0 ? 'Player 2' : 'Player 1'} Wins!`,
            canvas.width / 2,
            canvas.height / 2
        );
        return;
    }

    requestAnimationFrame(gameLoop);
}

// Start the game
gameLoop();
