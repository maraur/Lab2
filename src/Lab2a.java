

public class Lab2a {
    public static double[] simplifyShape(double[] poly, int k)
    {
        double[] resultPoly = poly;
        int leastValuable = 0;
        double tempVal = 0;
        double lastVal = 0;

        while(resultPoly.length > k){
            //L - P = L1, P - R = L2, L - R = L3, L1 + L2 - L3 = val
            //todo figure out a nicer way... too many pythagoras?
            for (int i = 1; i < poly.length/2 - 1; i++){ //ignores end points
                tempVal = Math.sqrt(Math.pow((poly[(i-1)*2] - poly[i*2]), 2.0) + Math.pow(poly[(i-1)*2+1] - poly[i*2+1], 2.0)); //L1 = L-P
                tempVal += Math.sqrt(Math.pow((poly[i*2] - poly[(i+1)*2]), 2.0) + Math.pow(poly[(i*2)+1] - poly[(i+1)*2+1], 2.0)); //L2 = P-R
                tempVal -= Math.sqrt(Math.pow((poly[(i-1)*2] - poly[(i+1)*2]), 2.0) + Math.pow(poly[(i-1)*2+1] - poly[(i+1)*2+1], 2.0)); //L3 = L-R
                if ( tempVal < lastVal ){
                    leastValuable = i;
                    lastVal = tempVal;
                }
            }
            //todo there must be a better way
            int index = 0;
            double[] tempPoly = new double[resultPoly.length -2]; //temp list with correct size
            for( int i = 0; i < resultPoly.length/2-1; i++ ){ // adds all but the least useful element into a new array
                if (!( i == leastValuable*2 || i == leastValuable*2 + 1 )){
                    index ++;
                    tempPoly[index] = resultPoly[i];
                }
            }
            resultPoly = tempPoly;
        }
        return resultPoly;
    }
}
