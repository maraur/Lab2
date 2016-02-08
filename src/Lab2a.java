

public class Lab2a {
    public static double[] simplifyShape(double[] poly, int k)
    {
        double[] resultPoly = poly;
        int leastValuable = 0;
        double tempVal;
        double lastVal;

        while(resultPoly.length > k*2){
            lastVal = 10000;
            //L - P = L1, P - R = L2, L - R = L3, L1 + L2 - L3 = val
            for (int i = 1; i < resultPoly.length/2 - 1; i++){ //ignores end points
                tempVal = Math.hypot((resultPoly[(i-1)*2] - resultPoly[i*2]), resultPoly[(i-1)*2+1] - resultPoly[i*2+1]);
                tempVal += Math.hypot((resultPoly[i*2] - resultPoly[(i+1)*2]), resultPoly[(i*2)+1] - resultPoly[(i+1)*2+1]);
                tempVal -= Math.hypot((resultPoly[(i-1)*2] - resultPoly[(i+1)*2]), resultPoly[((i-1)*2)+1] - resultPoly[(i+1)*2+1]);
                if ( tempVal < lastVal ){
                    leastValuable = i;
                    lastVal = tempVal;
                    if(tempVal == 0){ // if 0, no point in looping anymore
                        break;
                    }
                }
            }
            int index = 0;
            double[] tempPoly = new double[resultPoly.length-2]; //temp list with correct size
            for( int i = 0; i < resultPoly.length; i++ ){ // adds all but the least useful element into a new array
                if (!( i == leastValuable*2 || i == leastValuable*2 + 1 )){
                    tempPoly[index] = resultPoly[i];
                    index++;
                }
            }
            resultPoly = tempPoly;
        }
        return resultPoly;
    }
}
