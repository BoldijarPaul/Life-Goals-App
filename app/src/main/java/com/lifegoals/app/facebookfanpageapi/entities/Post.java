package com.lifegoals.app.facebookfanpageapi.entities;

import android.graphics.Bitmap;


import com.lifegoals.app.facebookfanpageapi.helper.DateHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Paul on 4/1/2015.
 */
public class Post {
    private String message;
    private String objectId;
    private Date createdDate;
    private String createdDateString;
    private String picture;

    // other fields
    private boolean loadingImage = false;
    private transient Bitmap bitmap;
    private boolean highlighted = false;


    public JSONObject toJsonObject() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", message);
            jsonObject.put("objectId", objectId);
            jsonObject.put("picture", picture);
            jsonObject.put("createdDateString", createdDateString);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Post fromJsonObject(JSONObject jsonObject) {
        try {
            Post post = new Post();
            String message = null;
            String createdDateString = null;
            try {
                message = jsonObject.getString("message");
            } catch (JSONException e) {
            }
            try {
                createdDateString = jsonObject.getString("createdDateString");
            } catch (JSONException e) {
            }
            post.setCreatedDateString(createdDateString);
            post.setMessage(message); // a message can be empty
            post.setObjectId(jsonObject.getString("objectId"));
            post.setPicture(jsonObject.getString("picture"));
            return post;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void recycleBitmap() {
        /* we don't want to recycle highlighted or null bitmaps */
        if (bitmap != null && !highlighted) {
            bitmap.recycle();
            bitmap = null;
        }
    }
    // getters / setters


    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public boolean isLoadingImage() {
        return loadingImage;
    }

    public void setLoadingImage(boolean loadingImage) {
        this.loadingImage = loadingImage;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }


    public void setCreatedDate(String createdDateString) {
        this.createdDateString = createdDateString;
        try {
            this.createdDate = DateHelper.getDateFormat().parse(createdDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    public void setCreatedDateString(String createdDateString) {
        this.createdDateString = createdDateString;
        if (createdDateString != null) {
            setCreatedDate(createdDateString);
        }
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
