package by.zemich.newsms.api.controller.dto.response;


import lombok.Data;

@Data
public class ErrorResponse {

	private String errorCode;
	private String message;
	private String details;


	public ErrorResponse(String errorCode, String message, String details) {
		this.errorCode = errorCode;
		this.message = message;
		this.details = details;
	}

	public ErrorResponse(String errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

	public static ErrorResponse of(
			String errorCode,
			String message,
			String details
	) {
		return new ErrorResponse(errorCode, message, details);
	}

	public static ErrorResponse of(
			String errorCode,
			String message
	) {
		return new ErrorResponse(errorCode, message);
	}
}