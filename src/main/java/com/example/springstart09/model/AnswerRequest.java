package com.example.springstart09.model;
import jakarta.validation.constraints.NotBlank;

public class AnswerRequest {
    @NotBlank(message = "Đáp án không được để trống") // cái này phải đi với @valid
    private String answer;

    public AnswerRequest() { // default constructor for deserialization
    }

    public String getAnswer() { // getter method for the 'answer' field
        return answer;
    }

    public void setAnswer(String answer) { // Vì hàm này không cần return gì chỉ để gửi dữ liệu nên để là void, setter method for the 'answer' field
        this.answer = answer;
    }
}