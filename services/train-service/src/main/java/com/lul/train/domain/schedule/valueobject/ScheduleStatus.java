package com.lul.train.domain.schedule.valueobject;

public enum ScheduleStatus {
    SCHEDULED,  // Lịch trình đã tạo nhưng chưa active
    ACTIVE,     // Đang hoạt động, có thể đặt vé
    CANCELLED,  // Đã hủy
    COMPLETED   // Đã hoàn thành (tàu đã chạy xong)
}
