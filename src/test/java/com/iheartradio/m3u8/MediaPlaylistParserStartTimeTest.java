package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.MediaPlaylist;
import com.iheartradio.m3u8.data.Playlist;
import com.iheartradio.m3u8.data.TrackData;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MediaPlaylistParserStartTimeTest {

    @Test
    public void testDatetimeV7_valid() throws Exception {
        String sourceFileToParse = "masterPlaylist-withdatetime-v7.m3u8";

        final Playlist playlist = TestUtil.parsePlaylistFromResource(sourceFileToParse);
        final MediaPlaylist mediaPlaylist = playlist.getMediaPlaylist();

        assertFalse(playlist.hasMasterPlaylist());
        assertTrue(playlist.hasMediaPlaylist());

        assertTrue(mediaPlaylist.hasStartData());
        assertEquals(-4.5, mediaPlaylist.getStartData().getTimeOffset(), 1e-12);
        assertTrue(mediaPlaylist.getStartData().isPrecise());
        assertEquals(10, mediaPlaylist.getTargetDuration());
        
        // verify version line: #EXT-X-VERSION:7
        assertEquals(7, playlist.getCompatibilityVersion());

        String programDateTime = null;
        for (TrackData trackData : mediaPlaylist.getTracks()) {
            // typically the first track may hold the EXT-X-PROGRAM-DATE-TIME
            if (trackData.getProgramDateTime() != null) {
                programDateTime = trackData.getProgramDateTime();
            }
        }
        
        // verify the program datetime line:  #EXT-X-PROGRAM-DATE-TIME
        assertEquals("2018-07-07T04:07:01.007Z", programDateTime);        
    }

    @Test(expected = ParseException.class)
    public void testDatetimeV8_fails() throws Exception {
        String sourceFileToParse = "masterPlaylist-withdatetime-v8.m3u8";

        // will throw because file has a too-high version line:  #EXT-X-VERSION:8
        final Playlist playlist = TestUtil.parsePlaylistFromResource(sourceFileToParse);
    }

}
