package com.DBDKillerTimer.main;

/**
 * ConvertToBinding.java.
 * @version 1.0
 * This class simply converts passed binds to integers so they
 * can be identified by a code.
 * @author Dafydd-Rhys Maund
 */
public class ConvertToBinding {

    /** VC_A through VC_Z. */
    public static final int VC_A = 0x001E;
    public static final int VC_B = 0x0030;
    public static final int VC_C = 0x002E;
    public static final int VC_D = 0x0020;
    public static final int VC_E = 0x0012;
    public static final int VC_F = 0x0021;
    public static final int VC_G = 0x0022;
    public static final int VC_H = 0x0023;
    public static final int VC_I = 0x0017;
    public static final int VC_J = 0x0024;
    public static final int VC_K = 0x0025;
    public static final int VC_L = 0x0026;
    public static final int VC_M = 0x0032;
    public static final int VC_N = 0x0031;
    public static final int VC_O = 0x0018;
    public static final int VC_P = 0x0019;
    public static final int VC_Q = 0x0010;
    public static final int VC_R = 0x0013;
    public static final int VC_S = 0x001F;
    public static final int VC_T = 0x0014;
    public static final int VC_U = 0x0016;
    public static final int VC_V = 0x002F;
    public static final int VC_W = 0x0011;
    public static final int VC_X = 0x002D;
    public static final int VC_Y = 0x0015;
    public static final int VC_Z = 0x002C;

    /** VC_1 through VC_9. */
    public static final int VC_1 = 0x0002;
    public static final int VC_2 = 0x0003;
    public static final int VC_3 = 0x0004;
    public static final int VC_4 = 0x0005;
    public static final int VC_5 = 0x0006;
    public static final int VC_6 = 0x0007;
    public static final int VC_7 = 0x0008;
    public static final int VC_8 = 0x0009;
    public static final int VC_9 = 0x000A;
    public static final int VC_0 = 0x000B;

    /**
     * Sets the bind using a char to int conversion.
     * @param decisiveTimer decisive strike timer bind
     * @param chaseTimer chase timer bind
     * @param onShoulderTimer on shoulder bind
     * @param resetTimer reset timers bind
     */
    public ConvertToBinding(final char decisiveTimer, final char chaseTimer,
                            final char onShoulderTimer, final char resetTimer) {
        KeyInput.setBindDecisive(setBind(decisiveTimer));
        KeyInput.setBindChase(setBind(chaseTimer));
        KeyInput.setBindShoulder(setBind(onShoulderTimer));
        KeyInput.setBindRestart(setBind(resetTimer));
    }

    /**
     * this method simply converts the bind to a code
     * then sets this code to the bind so it can be read in
     * Key Input.
     * @param bind the passed bind
     * @return the bind converted to code
     */
    private int setBind(final char bind) {
        int code = 0;

        //letters
        if (bind == 'A') {
            code = VC_A;
        } else if (bind == 'B') {
            code = VC_B;
        } else if (bind == 'C') {
            code = VC_C;
        } else if (bind == 'D') {
            code = VC_D;
        } else if (bind == 'E') {
            code = VC_E;
        } else if (bind == 'F') {
            code = VC_F;
        } else if (bind == 'G') {
            code = VC_G;
        } else if (bind == 'H') {
            code = VC_H;
        } else if (bind == 'I') {
            code = VC_I;
        } else if (bind == 'J') {
            code = VC_J;
        } else if (bind == 'K') {
            code = VC_K;
        } else if (bind == 'L') {
            code = VC_L;
        } else if (bind == 'M') {
            code = VC_M;
        } else if (bind == 'N') {
            code = VC_N;
        } else if (bind == 'O') {
            code = VC_O;
        } else if (bind == 'P') {
            code = VC_P;
        } else if (bind == 'Q') {
            code = VC_Q;
        } else if (bind == 'R') {
            code = VC_R;
        } else if (bind == 'S') {
            code = VC_S;
        } else if (bind == 'T') {
            code = VC_T;
        } else if (bind == 'U') {
            code = VC_U;
        } else if (bind == 'V') {
            code = VC_V;
        } else if (bind == 'W') {
            code = VC_W;
        } else if (bind == 'X') {
            code = VC_X;
        } else if (bind == 'Y') {
            code = VC_Y;
        } else if (bind == 'Z') {
            code = VC_Z;
        } else if (bind == '1') {
            code = VC_1;
        } else if (bind == '2') {
            code = VC_2;
        } else if (bind == '3') {
            code = VC_3;
        } else if (bind == '4') {
            code = VC_4;
        } else if (bind == '5') {
            code = VC_5;
        } else if (bind == '6') {
            code = VC_6;
        } else if (bind == '7') {
            code = VC_7;
        } else if (bind == '8') {
            code = VC_8;
        } else if (bind == '9') {
            code = VC_9;
        } else if (bind == '0') {
            code = VC_0;
        }

        return code;
    }
}
