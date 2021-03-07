package yhb.dc.demo.aidl.auto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * created by yaohaibiao on 2021/3/7.
 */
public class Music implements Parcelable {

    public String name;
    public int musicId;

    public Music(String name, int musicId) {
        this.name = name;
        this.musicId = musicId;
    }

    public Music() {

    }

    protected Music(Parcel in) {
        name = in.readString();
        musicId = in.readInt();
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(musicId);
    }

    public void readFromParcel(Parcel in) {
        name = in.readString();
        musicId = in.readInt();
    }

    @Override
    public String toString() {
        return "Music{" +
                "name='" + name + '\'' +
                ", musicId=" + musicId +
                '}';
    }
}
