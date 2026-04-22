// ============================================================
//  PRODUCT LISTING PAGE
// ============================================================

/**
 * Fetches products by type (e.g., "women" or "men") from the Spring Boot backend
 * and displays them in the HTML container.
 */
function loadProducts(productType) {
    const apiUrl = `http://localhost:8080/api/products/type/${productType}`;
    const container = document.getElementById('products-container');

    container.innerHTML = "<p>Loading products...</p>";

    fetch(apiUrl)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Failed to fetch products. Status: ${response.status}`);
            }
            return response.json();
        })
        .then(products => {
            container.innerHTML = "";

            if (products.length === 0) {
                container.innerHTML = `<p>No products found for ${productType}.</p>`;
                return;
            }

            products.forEach(product => {
                container.innerHTML += `
                    <div class="product-card" onclick="goToDetails(${product.id})">
                        <img src="${product.image}" alt="${product.category}" class="product-image">
                        <h3 style="text-transform: capitalize;">${product.category}</h3>
                        <p>${product.price.toFixed(2)} DT</p>
                    </div>
                `;
            });
        })
        .catch(error => {
            console.error("Error fetching products:", error);
            container.innerHTML = `<p style="color: red; grid-column: 1 / -1; text-align: center;">
                Error loading products. Please make sure your Spring Boot backend is running!
            </p>`;
        });
}

/**
 * Redirects the user to the product details page, passing the product ID in the URL.
 */
function goToDetails(productId) {
    window.location.href = `detailsProduct.html?id=${productId}`;
}


// ============================================================
//  PRODUCT DETAILS PAGE + KAFKA TRIGGER
// ============================================================

/**
 * Fetches a single product by its ID and displays it on the details page.
 * Then triggers the Kafka pipeline and polls for AI recommendations.
 */
function loadProductDetails() {
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get('id');
    const container = document.getElementById("product-details");

    if (!productId) {
        container.innerHTML = "<p>Product not found. No ID provided in the URL.</p>";
        return;
    }

    container.innerHTML = "<p>Loading product details...</p>";

    fetch(`http://localhost:8080/api/products/${productId}`)
        .then(response => {
            if (!response.ok) throw new Error("Product not found in database");
            return response.json();
        })
        .then(product => {
            // Render product details
            container.innerHTML = `
                <div class="product-details-wrapper">
                    <img src="${product.image}" alt="${product.category}" class="details-image">
                    <div class="details-info">
                        <h2>${product.category}</h2>
                        <h3 class="details-price">${product.price.toFixed(2)} DT</h3>
                        <p><strong>Type:</strong> <span style="text-transform: capitalize;">${product.type}</span></p>
                        <p><strong>Color:</strong> <span style="text-transform: capitalize;">${product.color}</span></p>
                        <p><strong>Motif:</strong> <span style="text-transform: capitalize;">${product.motif}</span></p>
                        <p><strong>Category:</strong> <span style="text-transform: capitalize;">${product.category}</span></p>
                        <button class="add-to-cart-btn">Add to Cart</button>
                        <button onclick="likeProduct(${product.id})" class="btn-like">
                          ❤️ Like
                        </button>
                    </div>
                </div>
            `;

            // --- KAFKA TRIGGER ---
            return fetch(`http://localhost:8080/api/interactions/${productId}/click`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(product)
            });
        })
        .then(response => {
            if (response.ok) {
                console.log("✅ Click event sent to Kafka successfully");
                pollForRecommendations();
            } else {
                console.error(`❌ Failed to send click event. Status: ${response.status}`);
                loadAIRecommendationsOnce();
            }
        })
        .catch(error => {
            console.error("Error:", error);
            container.innerHTML = "<p>Product not found. Please try returning to the shop.</p>";
        });
}


// ============================================================
//  AI RECOMMENDATIONS (Details Page — with Smart Polling)
// ============================================================

/**
 * Polls for NEW recommendations by comparing recommendation IDs.
 * First captures the current state, then waits for it to change.
 * This prevents showing stale/old recommendations.
 */
function pollForRecommendations() {
    const userId = 1;
    const container = document.getElementById('ai-recommendations-container');
    if (!container) return;

    // Show loading spinner
    container.innerHTML = `
        <div style="grid-column: 1 / -1; text-align: center; padding: 20px;">
            <p>Analyzing your preferences...</p>
            <div class="recommendation-spinner"></div>
        </div>
    `;

    // Snapshot the CURRENT recommendation ID before Service B processes
    fetch(`http://localhost:8080/api/recommendations/${userId}/latest-timestamp`)
        .then(res => res.json())
        .then(currentState => {
            const oldId = currentState.id; // Could be null if no recs exist yet
            console.log(`📌 Current recommendation ID before Kafka: ${oldId}`);

            // Start polling until the ID changes (meaning fresh recs arrived)
            let attempts = 0;
            const maxAttempts = 10;
            const delay = 2000;

            function tryFetch() {
                attempts++;
                console.log(`🔄 Polling attempt ${attempts}/${maxAttempts}...`);

                fetch(`http://localhost:8080/api/recommendations/${userId}/latest-timestamp`)
                    .then(res => res.json())
                    .then(newState => {
                        if (newState.id !== null && newState.id !== oldId) {
                            console.log(`✅ New recommendations detected! (ID: ${oldId} → ${newState.id})`);
                            fetchAndRenderRecommendations(userId, container);
                        } else if (attempts < maxAttempts) {
                            console.log("No new recommendations yet, retrying...");
                            setTimeout(tryFetch, delay);
                        } else {
                            console.log("Max attempts reached, showing existing recommendations");
                            fetchAndRenderRecommendations(userId, container);
                        }
                    })
                    .catch(err => {
                        console.error("Polling error:", err);
                        if (attempts < maxAttempts) {
                            setTimeout(tryFetch, delay);
                        } else {
                            container.innerHTML = `
                                <p style="grid-column: 1 / -1; text-align: center;">
                                    Could not load recommendations.
                                </p>
                            `;
                        }
                    });
            }

            setTimeout(tryFetch, 3000);
        })
        .catch(err => {
            console.error("Could not get current recommendation state:", err);
            setTimeout(() => fetchAndRenderRecommendations(userId, container), 5000);
        });
}

/**
 * Fetches and renders recommendations (called once we know they're fresh).
 */
function fetchAndRenderRecommendations(userId, container) {
    fetch(`http://localhost:8080/api/recommendations/${userId}`)
        .then(res => {
            if (!res.ok) throw new Error(`Status: ${res.status}`);
            return res.json();
        })
        .then(products => {
            if (products.length > 0) {
                console.log(`Rendering ${products.length} recommendations:`, products.map(p => p.id));
                renderRecommendationCards(container, products);
            } else {
                container.innerHTML = `
                    <p style="grid-column: 1 / -1; text-align: center;">
                        No recommendations yet. Keep browsing to train the engine!
                    </p>
                `;
            }
        })
        .catch(err => {
            console.error("Error fetching recommendations:", err);
            container.innerHTML = `
                <p style="grid-column: 1 / -1; text-align: center;">
                    Could not load recommendations.
                </p>
            `;
        });
}

/**
 * One-time fetch for existing recommendations (no polling).
 * Used as fallback when Kafka send fails.
 */
function loadAIRecommendationsOnce() {
    const userId = 1;
    const container = document.getElementById('ai-recommendations-container');
    if (!container) return;

    fetch(`http://localhost:8080/api/recommendations/${userId}`)
        .then(res => {
            if (!res.ok) throw new Error(`Status: ${res.status}`);
            return res.json();
        })
        .then(products => {
            if (products.length > 0) {
                renderRecommendationCards(container, products);
            } else {
                container.innerHTML = `<p>No recommendations yet.</p>`;
            }
        })
        .catch(err => {
            console.error("Error:", err);
            container.innerHTML = `<p>Could not load recommendations.</p>`;
        });
}

/**
 * Renders product cards into a container element.
 * Reusable for both detail page and home page recommendations.
 */
function renderRecommendationCards(container, products) {
    container.innerHTML = "";
    products.forEach(product => {
        const card = document.createElement('div');
        card.className = 'product-card';
        card.style.cursor = 'pointer';
        card.onclick = () => goToDetails(product.id);
        card.innerHTML = `
            <img src="${product.image}" alt="${product.category}" class="product-image">
            <h3 style="text-transform: capitalize;">${product.category}</h3>
            <p>${product.price.toFixed(2)} DT</p>
        `;
        container.appendChild(card);
    });
}


// ============================================================
//  LIKE FUNCTIONALITY
// ============================================================

/**
 * Sends a "like" event to the backend.
 */
function likeProduct(productId) {
    const requestBody = {
        userId: 1,
        productId: productId,
        action: "like"
    };

    fetch('http://localhost:8080/api/events/event', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(requestBody)
    })
    .then(response => {
        if (response.ok) {
            alert("Added to your favorites!");
        }
    })
    .catch(error => console.error("Error liking product:", error));
}


// ============================================================
//  HOME PAGE — Liked Products
// ============================================================

/**
 * Fetches the liked products for the user and displays them on the home page.
 */
function loadLikedProducts() {
    const userId = 1;
    const container = document.getElementById('liked-products-container');

    if (!container) return;

    container.innerHTML = "<p>Loading your favorite items...</p>";

    fetch(`http://localhost:8080/api/events/liked/${userId}`)
        .then(res => {
            if (!res.ok) throw new Error("Failed to fetch liked products");
            return res.json();
        })
        .then(products => {
            if (products.length === 0) {
                container.innerHTML = "<p>You haven't liked any products yet. Go explore the shop!</p>";
                return;
            }

            container.innerHTML = "";
            products.forEach(product => {
                container.innerHTML += `
                    <div class="product-card" style="cursor: pointer;" onclick="goToDetails(${product.id})">
                        <img src="${product.image}" alt="${product.category}" class="product-image">
                        <h3 style="text-transform: capitalize;">${product.category}</h3>
                        <p>${product.price.toFixed(2)} DT</p>
                    </div>
                `;
            });
        })
        .catch(err => {
            console.error("Error loading liked products:", err);
            container.innerHTML = "<p>Error loading your favorites.</p>";
        });
}


// ============================================================
//  HOME PAGE — Recommended Products
// ============================================================

/**
 * Fetches AI-recommended products for the user from Service B's output
 * and displays them on the home page.
 */
function loadRecommendedProducts() {
    const userId = 1;
    const container = document.getElementById('recommended-products-container');

    if (!container) return;

    container.innerHTML = "<p>Loading recommendations...</p>";

    fetch(`http://localhost:8080/api/recommendations/${userId}`)
        .then(res => {
            if (!res.ok) throw new Error(`Failed to fetch recommendations. Status: ${res.status}`);
            return res.json();
        })
        .then(products => {
            if (products.length === 0) {
                container.innerHTML = "<p>No recommendations yet. Browse some products first!</p>";
                return;
            }

            renderRecommendationCards(container, products);
        })
        .catch(err => {
            console.error("Error loading recommendations:", err);
            container.innerHTML = "<p>Could not load recommendations.</p>";
        });
}