const loadButton = document.getElementById("load-button");
const message = document.getElementById("message");
const cardList = document.getElementById("card-list");
const keywordInput = document.getElementById("keyword");

async function loadCards() {
    loadButton.disabled = true;
    message.textContent = "Đang tải...";
    cardList.replaceChildren();

    try {
        const keyword = keywordInput.value.trim();

        const url = keyword === ""
            ? "/api/review"
            : `/api/review/filter?keyword=${encodeURIComponent(keyword)}`;

        const response = await fetch(url);

        if (!response.ok) {
            throw new Error("Không tải được danh sách.");
        }

        const cards = await response.json();

        for (const card of cards) {
            const item = document.createElement("li");

            const text = document.createElement("span");
            text.textContent = `${card.word}: ${card.meaning} `;

            const deleteButton = document.createElement("button");
            deleteButton.type = "button";
            deleteButton.textContent = "Xóa";

            deleteButton.addEventListener("click", async () => {
                await deleteCard(card.id, deleteButton);
            });
            const editButton = document.createElement("button");
            editButton.type = "button";
            editButton.textContent = "Sửa";

            editButton.addEventListener("click", () => {
                startEditing(card);
            });
            item.appendChild(text);
            item.appendChild(editButton);
            item.appendChild(deleteButton);
            cardList.appendChild(item);
        }

        message.textContent = cards.length === 0
            ? "Chưa có thẻ nào."
            : `Có ${cards.length} thẻ.`;
    } catch (error) {
        message.textContent = "Không tải được dữ liệu. Hãy thử lại.";
    } finally {
        loadButton.disabled = false;
    }
}

loadButton.addEventListener("click", loadCards);

const createForm = document.getElementById("create-form");
const wordInput = document.getElementById("word");
const meaningInput = document.getElementById("meaning");
const saveButton = document.getElementById("save-button");
const createMessage = document.getElementById("create-message");
const cancelEditButton = document.getElementById("cancel-edit-button");

let editingCardId = null;

async function createCard(event) {
    event.preventDefault();

    const isEditing = editingCardId !== null;

    const url = isEditing
            ? `/api/review/${editingCardId}`
            : "/api/review";

    const method = isEditing ? "PUT" : "POST";

    saveButton.disabled = true;
    cancelEditButton.disabled = true;
    createMessage.textContent = "Đang lưu...";

    try {
        const response = await fetch(url, {
            method: method,
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                word: wordInput.value.trim(),
                meaning: meaningInput.value.trim()
            })
        });

        if (!response.ok) {
            if (response.status === 409) {
                createMessage.textContent = "Từ này đã tồn tại.";
            } else if (response.status === 400) {
                createMessage.textContent =
                        "Dữ liệu chưa hợp lệ. Hãy kiểm tra từ và nghĩa.";
            } else if (response.status === 404) {
                createMessage.textContent =
                        "Thẻ không còn tồn tại. Hãy hủy sửa và tải lại danh sách.";
            } else {
                createMessage.textContent = "Không lưu được thẻ.";
            }

            return;
        }

        cancelEditing();

        createMessage.textContent = isEditing
                ? "Đã cập nhật thẻ!"
                : "Đã thêm thẻ!";

        await loadCards();
    } catch (error) {
        createMessage.textContent = "Không kết nối được đến server.";
    } finally {
        saveButton.disabled = false;
        cancelEditButton.disabled = false;
    }
}

createForm.addEventListener("submit", createCard);

async function deleteCard(id, button) {
    const confirmed = window.confirm("Bạn muốn xóa thẻ này?");

    if (!confirmed) {
        return;
    }

    button.disabled = true;

    try {
        const response = await fetch(`/api/review/${id}`, {
            method: "DELETE"
        });

        if (!response.ok) {
            throw new Error("Không xóa được thẻ.");
        }

        await loadCards();
    } catch (error) {
        message.textContent = "Không xóa được thẻ. Hãy thử lại.";
    } finally {
        button.disabled = false;
    }
}

function validateForm() {
    const word = wordInput.value.trim();
    const meaning = meaningInput.value.trim();

    saveButton.disabled = word === "" || meaning === "";
}

wordInput.addEventListener("input", validateForm);
meaningInput.addEventListener("input", validateForm);


const questionButton = document.getElementById("question-button");
const questionText = document.getElementById("question-text");
const answerForm = document.getElementById("answer-form");
const answerInput = document.getElementById("answer");
const checkButton = document.getElementById("check-button");
const reviewMessage = document.getElementById("review-message");

let currentCardId = null;

async function loadQuestion() {
    questionButton.disabled = true;
    checkButton.disabled = true;
    currentCardId = null;
    questionText.textContent = "Đang tải câu hỏi...";
    reviewMessage.textContent = "";
    answerForm.reset();

    try {
        const response = await fetch("/api/review/random/question");

        if (!response.ok) {
            questionText.textContent = response.status === 404
                ? "Chưa có thẻ để ôn tập."
                : "Không tải được câu hỏi.";
            return;
        }

        const question = await response.json();

        currentCardId = question.id;
        questionText.textContent = `Từ cần dịch: ${question.word}`;
        checkButton.disabled = false;
        answerInput.focus();
    } catch (error) {
        questionText.textContent = "Không kết nối được đến server.";
    } finally {
        questionButton.disabled = false;
    }
}

async function checkAnswer(event) {
    event.preventDefault();

    if (currentCardId === null) {
        return;
    }

    checkButton.disabled = true;
    questionButton.disabled = true;
    reviewMessage.textContent = "Đang kiểm tra...";

    try {
        const response = await fetch(
            `/api/review/${currentCardId}/check`,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    answer: answerInput.value.trim()
                })
            }
        );

        if (!response.ok) {
            if (response.status === 404) {
                currentCardId = null;
                reviewMessage.textContent =
                    "Thẻ không còn tồn tại. Hãy lấy câu hỏi mới.";
            } else {
                reviewMessage.textContent =
                    "Không kiểm tra được. Hãy kiểm tra nội dung đáp án.";
            }

            return;
        }

        const result = await response.json();

        reviewMessage.textContent = result.correct
            ? "Chính xác!"
            : "Chưa đúng. Hãy thử lại.";
    } catch (error) {
        reviewMessage.textContent = "Không kết nối được đến server.";
    } finally {
        checkButton.disabled = currentCardId === null;
        questionButton.disabled = false;
    }
}

questionButton.addEventListener("click", loadQuestion);
answerForm.addEventListener("submit", checkAnswer);

function startEditing(card) {
    editingCardId = card.id;

    wordInput.value = card.word;
    meaningInput.value = card.meaning;

    saveButton.textContent = "Lưu thay đổi";
    cancelEditButton.hidden = false;
    createMessage.textContent = "Đang sửa thẻ ID " + card.id;

    wordInput.focus();
}

function cancelEditing() {
    editingCardId = null;

    createForm.reset();
    saveButton.textContent = "Thêm thẻ";
    cancelEditButton.hidden = true;
    createMessage.textContent = "";
}

cancelEditButton.addEventListener("click", cancelEditing);