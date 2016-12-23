package Classes;

import android.app.Application;

/**
 * Created by YoAtom on 12/22/2016.
 */

public class Global extends Application {
    private User profileGlobal;

    public User getProfileGlobal() {
        return profileGlobal;
    }

    public void setProfileGlobal(User profileGlobal) {
        this.profileGlobal = profileGlobal;
    }
}
