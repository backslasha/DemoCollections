// IMusicManager.aidl
package yhb.dc.demo.aidl.auto;

// Declare any non-default types here with import statements
import yhb.dc.demo.aidl.auto.Music;

interface IMusicManager {
    void addMusic(in Music music);
    List<Music> getMusicList();
}