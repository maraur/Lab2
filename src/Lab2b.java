import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

public class Lab2b {
    public static double[] simplifyShape(double[] poly, int k) {
        DLList list = new DLList();
        Comparator<DLList.Node> comparator = new NodeComparator();
        PriorityQueue<DLList.Node> queue = new PriorityQueue<>(poly.length / 2 - 2, comparator); //maybe too long?
        //poly.length/2 - 2 might be better as end points maybe should be excluded
        /*
        double[] firstArray = {100000, poly[0], poly[1], 0};
        list.addFirst(firstArray); //Kan helt onödig? En idé vore att skita i dem helt eftersom de inte ska tas bort och lägg till dem i slutet bara
        */
        for (int i = 2; i < poly.length / 2 - 2; i = i + 2) { //skippa första och sista noden
            double valMeas = Math.hypot((poly[(i - 1) * 2] - poly[i * 2]), poly[(i - 1) * 2 + 1] - poly[i * 2 + 1]);
            valMeas += Math.hypot((poly[i * 2] - poly[(i + 1) * 2]), poly[(i * 2) + 1] - poly[(i + 1) * 2 + 1]);
            valMeas -= Math.hypot((poly[(i - 1) * 2] - poly[(i + 1) * 2]), poly[((i - 1) * 2) + 1] - poly[(i + 1) * 2 + 1]);
            double[] nodeArray = {valMeas, poly[i], poly[i + 1], i};
            list.addLast(nodeArray);
        }
    /*
    Vad behöver sparas i varje nod? X,Y och värdeindex? Eller bara värdeindex?
    Ordningen behöver bevaras för att figuren ska behålla sin form
    Behöver en comparator som ska jämföra noder, men hur ska man jämföra dem?
    Har ju svårt att jämföra deras elt när den är E
    Typkonvertera?
    EDIT: Det är så jag tänkt nu, se compare i NodeComparator
     */
        while (queue.size() > k - 2) { //k-2 eftersom vi kan lägga till start och slut precis innan vi returnerar
            DLList.Node nodeToRemove = queue.poll();
            int nodeIndex = (int) ((double[]) nodeToRemove.elt)[3]; //tar ut index ur noden, tar även bort noden
            list.remove(nodeToRemove);
            Iterator<DLList.Node> iter = queue.iterator();
            int nodesToChange = 2;
            while (iter.hasNext() && nodesToChange != 0) {
                if ((((double[]) iter.next().getPrev().elt)[3]) == nodeIndex - 1 || (((double[]) iter.next().getPrev().elt)[3]) == nodeIndex + 1) {
                    DLList.Node current = iter.next();
                    queue.remove(current);
                    double[] node = (double[]) current.elt;
                    double[] prevNode = (double[]) current.getPrev().elt;
                    double[] nextNode = (double[]) current.getNext().elt;
                    double valMeas = Math.hypot((prevNode[1] - node[1]), prevNode[2] - node[2]);
                    valMeas += Math.hypot((node[1] - nextNode[1]), node[2] - nextNode[2]);
                    valMeas -= Math.hypot((prevNode[1] - nextNode[1]), prevNode[2] - nextNode[2]);
                    node[0] = valMeas;
                    queue.add(current);
                    nodesToChange--;
                }
            }
        }
        //todo beräkna om den borttagna punktens närmaste grannars värdemått
        /*
        EDIT: verkar vara enda sättet eftersom kön inte uppdateras dynamiskt
         */
        //todo Lägg tillbaka alla kvarvarande element i en ny double[] som kan returneras
        /*
        Är det bästa sättet att använda en ny comparator och kö eller genom att helt loopa och hålla på
        Rent effektivitetsmässigt känns en ny comparator snabbare? Borde kunna lägga in dem i kön med en iterator
        och sen använda en iterator till för att få tillbaka koordinaterna till en double[] igen
         */
        Comparator<DLList.Node> indexComparator = new IndexComparator();
        PriorityQueue<DLList.Node> indexQueue = new PriorityQueue<>(k - 2, indexComparator);
        Iterator<DLList.Node> iter = queue.iterator();
        while (iter.hasNext()) {
            indexQueue.add(iter.next());
        }
        Iterator<DLList.Node> indexIter = indexQueue.iterator();
        double[] resultPoly = new double[k * 2];
        resultPoly[0] = poly[0];
        resultPoly[1] = poly[1];
        resultPoly[resultPoly.length - 2] = poly[poly.length - 2];
        resultPoly[resultPoly.length - 1] = poly[poly.length - 1];
        for (int i = 2; i < k - 1; i = i + 2) { //enklare för indexering
            if (indexIter.hasNext()) {
                DLList.Node current = indexIter.next();
                resultPoly[i] = ((double[]) current.elt)[1];
                resultPoly[i + 1] = ((double[]) current.elt)[2];
            }
        }

        return resultPoly; //TODO REMOVE!!!!!!!!!
    }

    //TODO Get this working, needed for making priorityqueue
    private static class NodeComparator implements Comparator<DLList.Node> {

        @Override
        public int compare(DLList.Node o1, DLList.Node o2) {
            double[] o1list = (double[]) o1.elt; // något i den här stilen för att kunna välja specifikt ur index?
            double[] o2list = (double[]) o2.elt;
            //Less efficient (?) but might be a better choice
            if (o1list[0] > o2list[0]) {
                return 1;
            } else if (o1list[0] < o2list[0]) {
                return -1;
            }
            //return (int)(o1list[0] - o2list[0]); //might be good, but if differences < 1 then it will returns 0 which is BAD
            return 0;
        }
    }

    private static class IndexComparator implements Comparator<DLList.Node> {

        @Override
        public int compare(DLList.Node o1, DLList.Node o2) {
            double[] o1list = (double[]) o1.elt; // något i den här stilen för att kunna välja specifikt ur index?
            double[] o2list = (double[]) o2.elt;
            //Less efficient (?) but might be a better choice
            if (o1list[3] > o2list[3]) {
                return 1;
            } else if (o1list[3] < o2list[3]) {
                return -1;
            }
            //return (int)(o1list[0] - o2list[0]); //might be good, but if differences < 1 then it will returns 0 which is BAD
            return 0;
        }
    }
}