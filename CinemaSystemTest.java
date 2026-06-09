import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import static org.junit.jupiter.api.Assertions.*;

public class CinemaSystemTest {

    private CinemaSystem system;

    @BeforeEach
    public void setUp() {
        system = new CinemaSystem();
    }

    // ==============================================================================
    // TESTY FUNKCJI 1: obliczCeneBiletu (4 testy)
    // ==============================================================================

    @Test
    public void testCenaStandardowa() {
        assertEquals(25.0, system.obliczCeneBiletu(25, "poniedziałek"), 0.001);
    }

    @Test
    public void testCenaZnizkaDlaDziecka() {
        assertEquals(17.5, system.obliczCeneBiletu(10, "piątek"), 0.001);
    }

    @Test
    public void testCenaZnizkaDlaSeniora() {
        assertEquals(20.0, system.obliczCeneBiletu(70, "sobota"), 0.001);
    }

    @Test
    public void testCenaTaniWtorek() {
        assertEquals(15.0, system.obliczCeneBiletu(30, "wtorek"), 0.001);
    }

    // ==============================================================================
    // TESTY FUNKCJI 2: rezerwujMiejsce (4 testy)
    // ==============================================================================

    @Test
    public void testRezerwacjaWolnegoMiejsca() {
        assertTrue(system.rezerwujMiejsce(1, 5, 5));
    }

    @Test
    public void testRezerwacjaZajetegoMiejsca() {
        system.rezerwujMiejsce(1, 5, 5);
        assertFalse(system.rezerwujMiejsce(1, 5, 5));
    }

    @Test
    public void testRezerwacjaBledneWymiarySali() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                system.rezerwujMiejsce(1, 15, 5);
            }
        });
    }

    @Test
    public void testRezerwacjaWieluMiejscWTymSamymSeansie() {
        assertTrue(system.rezerwujMiejsce(1, 1, 1));
        assertTrue(system.rezerwujMiejsce(1, 1, 2));
    }

    // ==============================================================================
    // TESTY FUNKCJI 3: dodajFilm (4 testy)
    // ==============================================================================

    @Test
    public void testDodajFilmPoprawnie() {
        assertTrue(system.dodajFilm("Incepcja", 148, 15));
    }

    @Test
    public void testDodajFilmPustyTytul() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                system.dodajFilm("", 120, 12);
            }
        });
    }

    @Test
    public void testDodajFilmUjemnyCzas() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                system.dodajFilm("Shrek", -90, 0);
            }
        });
    }

    @Test
    public void testDodajDuplikatFilmu() {
        system.dodajFilm("Avatar", 160, 12);
        assertFalse(system.dodajFilm("Avatar", 160, 12));
    }

    // ==============================================================================
    // TESTY FUNKCJI 4: anulujRezerwacje (4 testy)
    // ==============================================================================

    @Test
    public void testAnulowanieRezerwacjiSukces() {
        system.rezerwujMiejsce(1, 3, 3);
        String wynik = system.anulujRezerwacje(1, 5.0);
        assertEquals("Anulowano pomyślnie", wynik);
    }

    @Test
    public void testAnulowanieZaPozno() {
        system.rezerwujMiejsce(1, 3, 3);
        String wynik = system.anulujRezerwacje(1, 1.5);
        assertEquals("Za późno na anulowanie", wynik);
    }

    @Test
    public void testAnulowanieNieistniejacejRezerwacji() {
        String wynik = system.anulujRezerwacje(999, 10.0);
        assertEquals("Brak rezerwacji", wynik);
    }

    @Test
    public void testPonowneAnulowanieTejSamejRezerwacji() {
        system.rezerwujMiejsce(1, 3, 3);
        system.anulujRezerwacje(1, 5.0);
        String wynik = system.anulujRezerwacje(1, 5.0);
        assertEquals("Już anulowano", wynik);
    }

    // ==============================================================================
    // TESTY FUNKCJI 5: raportZajetosci (4 testy)
    // ==============================================================================

    @Test
    public void testRaportPustySeans() {
        assertEquals(0.0, system.raportZajetosci(99), 0.001);
    }

    @Test
    public void testRaportJednoMiejsce() {
        system.rezerwujMiejsce(2, 1, 1);
        assertEquals(1.0, system.raportZajetosci(2), 0.001);
    }

    @Test
    public void testRaportKilkaMiejsc() {
        system.rezerwujMiejsce(3, 1, 1);
        system.rezerwujMiejsce(3, 1, 2);
        system.rezerwujMiejsce(3, 1, 3);
        assertEquals(3.0, system.raportZajetosci(3), 0.001);
    }

    @Test
    public void testRaportPoAnulowaniuRezerwacji() {
        system.rezerwujMiejsce(4, 1, 1);
        system.anulujRezerwacje(1, 5.0);
        assertEquals(0.0, system.raportZajetosci(4), 0.001);
    }
}