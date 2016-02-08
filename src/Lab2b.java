import java.util.Comparator;
import java.util.PriorityQueue;




public class Lab2b {
    public static double[] simplifyShape(double[] poly, int k)
    {
        //todo beräkna värdemåttet för varje intern punkt
    /*
    Skulle det kunna vara ett alternativ att:
    Beräkna värdemått, lägg i double[], lägg även x,y och index?
    Möjligt med exempelvis
    */
        DLList list = new DLList();
        Comparator<DLList.Node> comparator = new NodeComparator();
        PriorityQueue<DLList.Node> queue = new PriorityQueue<DLList.Node>(poly.length/2, comparator); //maybe too long?
        //poly.length/2 - 2 might be better as end points maybe should be excluded

        double[] firstArray = {100000, poly[0], poly[1], 0};
        list.addFirst(firstArray); //Not sure if even needed...... Kanske ska skita i dem helt eftersom de inte ska tas bort

        for(int i = 2; i < poly.length-2; i = i+2){ //skippa första och sista noden
            double valMeas = Math.hypot((poly[(i-1)*2] - poly[i*2]), poly[(i-1)*2+1] - poly[i*2+1]);
            valMeas += Math.hypot((poly[i*2] - poly[(i+1)*2]), poly[(i*2)+1] - poly[(i+1)*2+1]);
            valMeas -= Math.hypot((poly[(i-1)*2] - poly[(i+1)*2]), poly[((i-1)*2)+1] - poly[(i+1)*2+1]);
            double[] nodeArray = {valMeas, poly[i], poly[i+1], i};
            list.addLast(nodeArray);
        }
    /*
    Vad behöver sparas i varje nod? X,Y och värdeindex? Eller bara värdeindex?
    Ordningen behöver bevaras för att figuren ska behålla sin form
    Behöver en comparator som ska jämföra noder, men hur ska man jämföra dem?
    Har ju svårt att jämföra deras elt när den är E
    Typkonvertera?
     */

        //todo while antalet punkter större än k
        //todo tag bort den minst värdefulla
        /*
        Ta bort den första i queue, med list.remove()?
         */
        //todo beräkna om den borttagna punktens närmaste grannars värdemått
        /*
        Leta reda på de punkter med index i-1 och i+1 (kommer ligga i [3] i deras lista), ta ut dem ur priorityqueue och
        räkna om deras värdemått, stoppa tillbaka dem igen?
         */

        return poly; //TODO REMOVE!!!!!!!!!
    }
    //TODO Get this working, needed for possibility of making priorityqueue
    private static class NodeComparator implements Comparator<DLList.Node>{

        @Override
        public int compare(DLList.Node o1, DLList.Node o2) {
            double[] o1list = (double[])o1.elt; // något i den här stilen för att kunna välja specifikt ur index?
            double[] o2list = (double[])o2.elt;
            /* Less efficient (?) but might be a better choice
            if(o1list[0] > o2list[0]){
                return 1;
            }else if(o1list[0] < o2list[0]){
                return -1;
            }*/
            //return (int)(o1list[0] - o2list[0]); //might be good, but if differences are < 1 then it will returns 0 which is BAD
            return 0;
        }
    }
}
