import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CinemaSystem {

    public Map<String, Film> filmy = new HashMap<>();
    public Map<Integer, Rezerwacja> rezerwacje = new HashMap<>();
    public Map<Integer, Set<String>> zajeteMiejsca = new HashMap<>();

    public static class Film {
        public String tytul;
        public int czasTrwania;
        public int minimalnyWiek;

        public Film(String tytul, int czasTrwania, int minimalnyWiek) {
            this.tytul = tytul;
            this.czasTrwania = czasTrwania;
            this.minimalnyWiek = minimalnyWiek;
        }
    }

    public static class Rezerwacja {
        public int rezerwacjaId;
        public int seansId;
        public int rzad;
        public int miejsce;
        public String status;

        public Rezerwacja(int rezerwacjaId, int seansId, int rzad, int miejsce, String status) {
            this.rezerwacjaId = rezerwacjaId;
            this.seansId = seansId;
            this.rzad = rzad;
            this.miejsce = miejsce;
            this.status = status;
        }
    }

    // --- FUNKCJA 1: Obliczanie ceny biletu ---
    public double obliczCeneBiletu(int wiek, String dzienTygodnia) {
        double cenaBazowa = 25.0;
        if (dzienTygodnia != null && dzienTygodnia.trim().equalsIgnoreCase("wtorek")) {
            return 15.0;
        }
        if (wiek < 12) {
            return cenaBazowa * 0.7;
        }
        if (wiek > 65) {
            return cenaBazowa * 0.8;
        }
        return cenaBazowa;
    }

    // --- FUNKCJA 2: Rezerwacja miejsca ---
    public boolean rezerwujMiejsce(int seansId, int rzad, int miejsce) {
        if (rzad <= 0 || miejsce <= 0 || rzad > 10 || miejsce > 10) {
            throw new IllegalArgumentException("Niepoprawny numer rzędu lub miejsca (zakres 1-10).");
        }

        if (!zajeteMiejsca.containsKey(seansId)) {
            zajeteMiejsca.put(seansId, new HashSet<>());
        }

        String kluczMiejsca = rzad + "-" + miejsce;

        if (zajeteMiejsca.get(seansId).contains(kluczMiejsca)) {
            return false;
        }

        zajeteMiejsca.get(seansId).add(kluczMiejsca);
        int rezerwacjaId = rezerwacje.size() + 1;
        rezerwacje.put(rezerwacjaId, new Rezerwacja(rezerwacjaId, seansId, rzad, miejsce, "aktywna"));
        return true;
    }

    // --- FUNKCJA 3: Dodawanie filmu do repertuaru ---
    public boolean dodajFilm(String tytul, int czasTrwania, int minimalnyWiek) {
        if (tytul == null || tytul.trim().isEmpty()) {
            throw new IllegalArgumentException("Tytuł filmu nie może być pusty.");
        }
        if (czasTrwania <= 0) {
            throw new IllegalArgumentException("Czas trwania musi być dodatni.");
        }
        if (filmy.containsKey(tytul)) {
            return false;
        }

        filmy.put(tytul, new Film(tytul, czasTrwania, minimalnyWiek));
        return true;
    }

    // --- FUNKCJA 4: Anulowanie rezerwacji ---
    public String anulujRezerwacje(int rezerwacjaId, double godzinyDoSeansu) {
        if (!rezerwacje.containsKey(rezerwacjaId)) {
            return "Brak rezerwacji";
        }
        Rezerwacja rez = rezerwacje.get(rezerwacjaId);
        if (rez.status.equals("anulowana")) {
            return "Już anulowano";
        }
        if (godzinyDoSeansu < 2.0) {
            return "Za późno na anulowanie";
        }

        String kluczMiejsca = rez.rzad + "-" + rez.miejsce;
        zajeteMiejsca.get(rez.seansId).remove(kluczMiejsca);
        rez.status = "anulowana";
        return "Anulowano pomyślnie";
    }

    // --- FUNKCJA 5: Generowanie raportu zajętości sali ---
    public double raportZajetosci(int seansId) {
        double pojemnoscSali = 100.0;
        if (!zajeteMiejsca.containsKey(seansId)) {
            return 0.0;
        }
        int liczbaZajetych = zajeteMiejsca.get(seansId).size();
        return Math.round((liczbaZajetych / pojemnoscSali) * 100.0 * 100.0) / 100.0;
    }
}