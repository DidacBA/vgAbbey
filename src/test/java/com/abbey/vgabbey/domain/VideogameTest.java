package com.abbey.vgabbey.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.abbey.vgabbey.web.rest.TestUtil;

public class VideogameTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Videogame.class);
        Videogame videogame1 = new Videogame();
        videogame1.setId(1L);
        Videogame videogame2 = new Videogame();
        videogame2.setId(videogame1.getId());
        assertThat(videogame1).isEqualTo(videogame2);
        videogame2.setId(2L);
        assertThat(videogame1).isNotEqualTo(videogame2);
        videogame1.setId(null);
        assertThat(videogame1).isNotEqualTo(videogame2);
    }
}
