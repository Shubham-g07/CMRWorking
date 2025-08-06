window.addEventListener("DOMContentLoaded", () => {
    const msg = document.getElementById("contactResponse")?.value?.trim();
    const type = document.getElementById("toastType")?.value || 'info';

    if (msg && msg.length > 0) {
        toastr.options = {
            "closeButton": true,
            "progressBar": true,
            "positionClass": "toast-top-center",
            "timeOut": "4000",
            "preventDuplicates": true,
            "newestOnTop": true
        };
        toastr[type](msg);
    }

});


document.addEventListener('DOMContentLoaded', function() {
    console.log('Contact form script loaded!');

    const cNameInput = document.getElementById('cName');
    const cWorkInput = document.getElementById('cWork');
    const cDescTextarea = document.getElementById('cDesc');
    const cImpDatesInput = document.getElementById('cImpDates');
    const summarizeDescBtn = document.getElementById('summarizeDescBtn');
    const suggestDatesBtn = document.getElementById('suggestDatesBtn');
    const descSpinner = document.getElementById('descSpinner');
    const datesSpinner = document.getElementById('datesSpinner');

    async function callGeminiApi(prompt, retries = 3, delay = 1000) {
        let chatHistory = [];
        chatHistory.push({ role: 'user', parts: [{ text: prompt }] });
        const payload = { contents: chatHistory };
        const apiKey = 'AIzaSyD2WYJ2EgMmwu7pPZ9Wr2Wf1A6NvgKNLrA';

        const apiUrl = `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-preview-05-20:generateContent?key=${apiKey}`;

        for (let i = 0; i < retries; i++) {
            try {
                const response = await fetch(apiUrl, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload),
                });

                if (!response.ok) {
                    if (response.status === 429 && i < retries - 1) {
                        // Too Many Requests
                        await new Promise((res) => setTimeout(res, delay));
                        delay *= 2; // Exponential backoff
                        continue;
                    }
                    throw new Error(`API error: ${response.status} ${response.statusText}`);
                }

                const result = await response.json();
                if (
                    result.candidates &&
                    result.candidates.length > 0 &&
                    result.candidates[0].content &&
                    result.candidates[0].content.parts &&
                    result.candidates[0].content.parts.length > 0
                ) {
                    return result.candidates[0].content.parts[0].text;
                } else {
                    throw new Error(
                        'Gemini API response structure unexpected or content missing.'
                    );
                }
            } catch (error) {
                console.error('Error calling Gemini API:', error);
                if (i === retries - 1) throw error; // Re-throw after last retry
                await new Promise((res) => setTimeout(res, delay));
                delay *= 2;
            }
        }
        return null; // Should not reach here if retries are exhausted and error is thrown
    }

    // ✨ Feature 1: Summarize Description
    if (summarizeDescBtn && cDescTextarea) {
        summarizeDescBtn.addEventListener('click', async function() {
            const description = cDescTextarea.value.trim();
            if (description.length < 20) {
                alert('Please enter a longer description to summarize.');
                return;
            }

            summarizeDescBtn.disabled = true;
            descSpinner.style.display = 'inline-block';

            try {
                const prompt = `Summarize the following contact description concisely, focusing on key details: "${description}"`;
                const summarizedText = await callGeminiApi(prompt);
                if (summarizedText) {
                    cDescTextarea.value = summarizedText;
                } else {
                    alert('Could not summarize the description. Please try again.');
                }
            } catch (error) {
                alert('Failed to summarize description: ' + error.message);
            } finally {
                summarizeDescBtn.disabled = false;
                descSpinner.style.display = 'none';
            }
        });
    }

    // ✨ Feature 2: Suggest Important Dates
    if (suggestDatesBtn && cImpDatesInput && cNameInput && cWorkInput) {
        suggestDatesBtn.addEventListener('click', async function() {
            const contactName = cNameInput.value.trim();
            const contactWork = cWorkInput.value.trim();

            if (!contactName) {
                alert('Please enter a Contact Name to suggest dates.');
                return;
            }

            suggestDatesBtn.disabled = true;
            datesSpinner.style.display = 'inline-block';

            try {
                let prompt = `Suggest a few important dates (e.g., Birthday, Anniversary, Work Anniversary, etc.) for a contact named "${contactName}".`;
                if (contactWork) {
                    prompt += ` Their profession is "${contactWork}".`;
                }
                prompt += ` Format the output as a comma-separated list, like "Birthday: MM/DD, Work Anniversary: MM/DD".`;

                const suggestedDates = await callGeminiApi(prompt);
                if (suggestedDates) {
                    // Append or replace based on existing content
                    if (cImpDatesInput.value.trim() !== '') {
                        cImpDatesInput.value += ', ' + suggestedDates;
                    } else {
                        cImpDatesInput.value = suggestedDates;
                    }
                } else {
                    alert('Could not suggest dates. Please try again.');
                }
            } catch (error) {
                alert('Failed to suggest dates: ' + error.message);
            } finally {
                suggestDatesBtn.disabled = false;
                datesSpinner.style.display = 'none';
            }
        });
    }

});