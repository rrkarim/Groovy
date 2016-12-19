package Classes;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by YoAtom on 11/5/2016.
 */

public class Post {
    private int id, likesCount, typeId, repCounts, authorId;
    private String title, headerImage, text, singer, album, track;
    private Bitmap imageBitmap;
    private Author author;
    private Date date;
    private Actions actions;

    public Post(int id, String title, String singer, String headerImage, int likesCount, int repCounts,
                int authorId, String album, int typeId, String track, String text, Author author, Date date, Actions actions) {
        this.id = id;
        this.title = title;
        this.singer = singer;
        this.headerImage = headerImage;
        this.likesCount = likesCount;
        this.repCounts = repCounts;
        this.authorId = authorId;
        this.album = album;
        this.typeId = typeId;
        this.text = text;
        this.author = author;
        this.date = date;
        this.actions = actions;
        this.track = track;
    }

    public int getId() {
        return id;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getTitle() {
        return title;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public String getText() {
        return text;
    }

    public void increaseCount() {
        this.likesCount++;
    }

    public int getRepCounts() {
        return repCounts;
    }

    public void increaseCount(int likes) {
        this.likesCount += likes;
    }

    public String getAuthorName() {
        return this.author.getName();
    }

    public String getAuthorSName() {
        return this.author.getSurname();
    }

    public String getSinger() {
        return this.singer;
    }

    public String getAuthorImage() {
        return this.author.getImage_small();
    }

    public Date getDate() {
        return this.date;
    }

    public String getTrack() {
        return this.track;
    }

    public boolean isLiked() {
        return (actions.isLiked == 1 ? true : false);
    }

    public boolean isPosted() {
        return (actions.isPosted == 1 ? true : false);
    }

    public boolean checkBitMap() {
        if(imageBitmap == null) return false;
        return true;
    }
    public Bitmap getImageBitmap() {
        return this.imageBitmap;
    }
    public void SetBitmap(Bitmap bitMap) {
        this.imageBitmap = bitMap;
    }
}
