package Interfaces;

import Classes.Post;

/**
 * Created by YoAtom on 12/16/2016.
 */

public interface MediaCallback {
    void trackCompleteCallback(Post track);
    void trackReadyCallback(Post track);
}
