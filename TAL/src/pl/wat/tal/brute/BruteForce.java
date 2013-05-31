package pl.wat.tal.brute;

import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.DepthFirstIterator;
import pl.wat.tal.misc.TSPResult;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author k37
 */

public class BruteForce {
    private WeightedGraph<String, DefaultEdge> graph;
    private long bestDistance;

    public BruteForce(WeightedGraph<String, DefaultEdge> graph) {
        this.graph = graph;
        bestDistance = 0;
    }

    /**
     * @param start       poczatek trasy
     * @param destination cel trasy
     * @return droga + obliczona odleglosc
     */

    public TSPResult countRoad(String start, String destination) {
        TSPResult result = new TSPResult();
        List<String> route = createFirstRoute(start, destination);
        bestDistance = countDistance(route);  // pierwsza odleglosc

        List<LinkedList<String>> permutations;  // TODO znalezienie permutacji

        return result;
    }

    /**
     * Metoda tworzy pierwsza przykladowa droge
     *
     * @param start       poczatek trasy
     * @param destination cel trasy
     * @return lista wierzcholkow drogi
     * @author k37
     */

    private List<String> createFirstRoute(String start, String destination) {
        List<String> route = new LinkedList<String>();

        route.add(start);
        Iterator<String> i = new DepthFirstIterator<String, DefaultEdge>(graph);
        while (i.hasNext()) {
            String vertex = i.next();
            if (!vertex.equals(start) && !vertex.equals(destination)) {  // musi byc rozny od startu i konca
                route.add(vertex);  // dodanie kolejnego wierzcholka do drogi
            }
        }

        route.add(destination);
        route.add(start);

        return route;
    }

    /**
     * Metoda zwraca wszystkie permutacje zbioru. Zachowuje kolejnosc pierwszego i dwoch ostatnich elementow.
     *
     * @param route        lista z przykladowa droga
     * @param index        aktualny element permutacji
     * @param permutations lista zwrotna
     * @author k37
     */

    public void permute(List<String> route, int index, List<LinkedList<String>> permutations) {
        //List<LinkedList<String>> permutations = new LinkedList<LinkedList<String>>();

        if (index >= route.size() - 2) {  // koniec permutacji
            permutations.add((LinkedList<String>) route);
        } else {
            String element = route.get(index);
            for (int i = index + 1; i < route.size() - 2; i++) {
                // zamiana elemntow na pozycjach index oraz i
                route.set(index, route.get(i));
                route.set(i, element);

                // rekurencja
                permute(route, index + 1, permutations);

                // powrot do poprzedniej postaci
                route.set(i, route.get(index));
                route.set(index, element);
            }
        }

    }

    /**
     * Metoda obliczajaca dlugosc drogi
     *
     * @param route lista zawierajaca droge
     * @return dlugosc drogi
     * @author k37
     */

    private long countDistance(List<String> route) {
        long result = 0;
        String from = new String();
        String to = new String();

        Iterator<String> i = route.iterator();
        from = i.next();  // pierwszy wierzcholek
        to = i.next();  // pierwsze odwiedzane miasto

        while (i.hasNext()) {
            result = (long) (result + graph.getEdgeWeight(graph.getEdge(from, to)));  // dodanie wagi obecnej krawedzi
            from = to;  // zamiana koncowego na poczatkowy
            to = i.next();  // pobranie kolejnego wierzcholka
        }

        result = (long) (result + graph.getEdgeWeight(graph.getEdge(from, to)));  // ostatni wierzcholek poza petla
        return result;
    }
}
