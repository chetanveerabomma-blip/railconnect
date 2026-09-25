package com.railconnect.admin.dto;

import jakarta.validation.constraints.NotBlank;

public class VerifyTicketRequest {
    @NotBlank(message = "PNR, Ticket ID or QR payload is required")
    private String pnrOrTicketId;

    private Long stationId;
    private String status = "VERIFIED"; // VERIFIED, ABSENT, FRAUDULENT
    private String comments;

    public VerifyTicketRequest() {}

    public VerifyTicketRequest(String pnrOrTicketId, Long stationId, String status, String comments) {
        this.pnrOrTicketId = pnrOrTicketId;
        this.stationId = stationId;
        this.status = status;
        this.comments = comments;
    }

    public String getPnrOrTicketId() { return pnrOrTicketId; }
    public void setPnrOrTicketId(String pnrOrTicketId) { this.pnrOrTicketId = pnrOrTicketId; }

    public Long getStationId() { return stationId; }
    public void setStationId(Long stationId) { this.stationId = stationId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}
