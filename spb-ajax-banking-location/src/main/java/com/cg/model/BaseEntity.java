package com.cg.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@Accessors(chain = true)
public abstract class BaseEntity {

    @Column(columnDefinition = "boolean default false")
    private boolean deleted;
    @CreationTimestamp
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss Z", timezone = "Asia/Ho_Chi_Minh")
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;
    @CreatedBy
    @Column(name = "created_by")
    private Long createdBy;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    private Long updatedBy;


}
