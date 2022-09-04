package com.nearsg.jobportal.util;

import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;

public class EthUtil {
    private static final String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";
    private static final String MESSAGE = "Please sign this message for authentication on the postal";


    private static boolean verifyAddressFromSignature(String address, String signature) {
        String prefix = PERSONAL_MESSAGE_PREFIX + MESSAGE.length();
        byte[] msgHash = Hash.sha3((prefix + MESSAGE).getBytes());

        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }

        SignatureData sd =
                new SignatureData(
                        v,
                        (byte[]) Arrays.copyOfRange(signatureBytes, 0, 32),
                        (byte[]) Arrays.copyOfRange(signatureBytes, 32, 64));

        String addressRecovered = null;
        boolean match = false;

        // Iterate for each possible key to recover
        for (int i = 0; i < 4; i++) {
            BigInteger publicKey =
                    Sign.recoverFromSignature(
                            (byte) i,
                            new ECDSASignature(
                                    new BigInteger(1, sd.getR()), new BigInteger(1, sd.getS())),
                            msgHash);

            if (publicKey != null) {
                addressRecovered = "0x" + Keys.getAddress(publicKey);

                if (addressRecovered.equalsIgnoreCase(address)) {
                    match = true;
                    break;
                }
            }
        }

        return match;
    }

//    public static void main(String[] args) {
//        String address = "0x7eCFce95576a29671B726CC59413eBffD53032ee";
//        String signature = "0x7bdaf62be99ac912904e5ecc4a7fd5e60cd36719415243ddfe0879ae1c59b8227260dc5414525f8045d7038d69533ae800004bcfe066a7a49255aa5a34a1252d1b";
//        String pkey = "0x403347ead2490dd6128cb2323ce68d5e9c085ffc54693e7567036db1d34721c2";
//        System.out.println(EthUtil.verifyAddressFromSignature(address, signature));
//    }
}
