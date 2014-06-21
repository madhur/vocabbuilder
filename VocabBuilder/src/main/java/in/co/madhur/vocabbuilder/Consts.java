package in.co.madhur.vocabbuilder;

/**
 * Created by madhur on 19-Jun-14.
 */
public class Consts {

    public static final String PACKAGE="in.co.madhur.vocabbuilder";



    public enum LISTS
    {
        Home,
        A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z;



        public static String[] names() {
            LISTS[] states = values();
            String[] names = new String[states.length];

            for (int i = 0; i < states.length; i++) {
                names[i] = states[i].name();
            }

            return names;
        }
    };

}
