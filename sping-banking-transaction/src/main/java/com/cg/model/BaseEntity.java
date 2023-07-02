package com.cg.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;
@MappedSuperclass
public abstract class BaseEntity {
    @CreationTimestamp
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss Z", timezone = "Asia/Ho_Chi_Minh")
    @Column(name = "created_at",  nullable = false, updatable = false)
    private Date createdAt = new Date();
}
