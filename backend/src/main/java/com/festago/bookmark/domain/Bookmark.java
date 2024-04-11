package com.festago.bookmark.domain;

import com.festago.common.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"bookmark_type", "resource_id", "member_id"}
        )
    }
)
public class Bookmark extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "bookmark_type")
    private BookmarkType bookmarkType;

    @Column(name = "resource_id")
    private Long resourceId;

    @Column(name = "member_id")
    private Long memberId;

    public Bookmark(Long id, BookmarkType bookmarkType, Long resourceId, Long memberId) {
        this.id = id;
        this.bookmarkType = bookmarkType;
        this.resourceId = resourceId;
        this.memberId = memberId;
    }

    public Bookmark(BookmarkType bookmarkType, Long resourceId, Long memberId) {
        this(null, bookmarkType, resourceId, memberId);
    }

    public Long getId() {
        return id;
    }

    public BookmarkType getBookmarkType() {
        return bookmarkType;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
