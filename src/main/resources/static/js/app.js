const API_BASE_URL = window.location.origin;

const elements = {
    form: document.getElementById('shortenForm'),
    longUrlInput: document.getElementById('longUrl'),
    expirationSelect: document.getElementById('expirationDays'),
    shortenBtn: document.getElementById('shortenBtn'),
    btnText: document.querySelector('.btn-text'),
    btnLoader: document.querySelector('.btn-loader'),
    result: document.getElementById('result'),
    error: document.getElementById('error'),
    errorMessage: document.getElementById('errorMessage'),
    shortUrlDisplay: document.getElementById('shortUrlDisplay'),
    originalUrl: document.getElementById('originalUrl'),
    createdAt: document.getElementById('createdAt'),
    expiresAt: document.getElementById('expiresAt'),
    expiresAtContainer: document.getElementById('expiresAtContainer'),
    copyBtn: document.getElementById('copyBtn'),
    newUrlBtn: document.getElementById('newUrlBtn')
};

// Form submission
elements.form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const longUrl = elements.longUrlInput.value.trim();
    const expirationDays = elements.expirationSelect.value;

    if (!longUrl) {
        showError('Please enter a valid URL');
        return;
    }

    await shortenUrl(longUrl, expirationDays);
});

// Shorten URL API call
async function shortenUrl(url, expirationDays) {
    try {
        setLoading(true);
        hideError();

        const requestBody = {
            url: url
        };

        if (expirationDays) {
            requestBody.expirationDays = parseInt(expirationDays);
        }

        const response = await fetch(`${API_BASE_URL}/api/shorten`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBody)
        });

        if (!response.ok) {
            throw new Error('Failed to shorten URL');
        }

        const data = await response.json();
        displayResult(data);

    } catch (error) {
        console.error('Error:', error);
        showError('Failed to shorten URL. Please try again.');
    } finally {
        setLoading(false);
    }
}

// Display result
function displayResult(data) {
    const shortUrl = `${API_BASE_URL}/s/${data.shortUrl}`;

    elements.shortUrlDisplay.value = shortUrl;
    elements.originalUrl.textContent = truncateUrl(data.longUrl, 50);
    elements.originalUrl.title = data.longUrl;
    elements.createdAt.textContent = formatDateTime(data.createdAt);

    if (data.expiresAt) {
        elements.expiresAt.textContent = formatDateTime(data.expiresAt);
        elements.expiresAtContainer.style.display = 'flex';
    } else {
        elements.expiresAtContainer.style.display = 'none';
    }

    elements.form.style.display = 'none';
    elements.result.style.display = 'block';
}

// Copy to clipboard
elements.copyBtn.addEventListener('click', async () => {
    const shortUrl = elements.shortUrlDisplay.value;

    try {
        await navigator.clipboard.writeText(shortUrl);

        // Visual feedback
        const originalHTML = elements.copyBtn.innerHTML;
        elements.copyBtn.innerHTML = `
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                <path d="M20 6L9 17l-5-5" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            Copied!
        `;
        elements.copyBtn.classList.add('copied');

        setTimeout(() => {
            elements.copyBtn.innerHTML = originalHTML;
            elements.copyBtn.classList.remove('copied');
        }, 2000);

    } catch (error) {
        console.error('Failed to copy:', error);
        showError('Failed to copy to clipboard');
    }
});

// New URL button
elements.newUrlBtn.addEventListener('click', () => {
    elements.form.style.display = 'block';
    elements.result.style.display = 'none';
    elements.longUrlInput.value = '';
    elements.expirationSelect.value = '';
    elements.longUrlInput.focus();
});

// Helper functions
function setLoading(isLoading) {
    elements.shortenBtn.disabled = isLoading;
    if (isLoading) {
        elements.btnText.style.display = 'none';
        elements.btnLoader.style.display = 'block';
    } else {
        elements.btnText.style.display = 'block';
        elements.btnLoader.style.display = 'none';
    }
}

function showError(message) {
    elements.errorMessage.textContent = message;
    elements.error.style.display = 'flex';

    setTimeout(() => {
        hideError();
    }, 5000);
}

function hideError() {
    elements.error.style.display = 'none';
}

function formatDateTime(dateTimeString) {
    const date = new Date(dateTimeString);
    const options = {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    };
    return date.toLocaleDateString('en-US', options);
}

function truncateUrl(url, maxLength) {
    if (url.length <= maxLength) {
        return url;
    }
    return url.substring(0, maxLength) + '...';
}

// Auto-focus on page load
window.addEventListener('load', () => {
    elements.longUrlInput.focus();
});

// Handle paste event
elements.longUrlInput.addEventListener('paste', (e) => {
    setTimeout(() => {
        const pastedText = elements.longUrlInput.value.trim();
        if (pastedText && !pastedText.startsWith('http://') && !pastedText.startsWith('https://')) {
            elements.longUrlInput.value = 'https://' + pastedText;
        }
    }, 10);
});

