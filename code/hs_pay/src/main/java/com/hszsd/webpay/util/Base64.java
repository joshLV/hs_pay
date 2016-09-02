package com.hszsd.webpay.util;

import sun.misc.BASE64Decoder;

import java.io.UnsupportedEncodingException;

public class Base64 {
	/** Field BASE64DEFAULTLENGTH */
	public static final int BASE64DEFAULTLENGTH = 76;
	static private final int BASELENGTH = 255;
	static private final int LOOKUPLENGTH = 64;
	static private final int TWENTYFOURBITGROUP = 24;
	static private final int EIGHTBIT = 8;
	static private final int SIXTEENBIT = 16;
	static private final int FOURBYTE = 4;
	static private final int SIGN = -128;
	static private final char PAD = '=';
	static private final boolean fDebug = false;
	static final private byte[] base64Alphabet = new byte[BASELENGTH];
	static final private char[] lookUpBase64Alphabet = new char[LOOKUPLENGTH];

	static {

		for (int i = 0; i < BASELENGTH; i++) {
			base64Alphabet[i] = -1;
		}
		for (int i = 'Z'; i >= 'A'; i--) {
			base64Alphabet[i] = (byte) (i - 'A');
		}
		for (int i = 'z'; i >= 'a'; i--) {
			base64Alphabet[i] = (byte) (i - 'a' + 26);
		}

		for (int i = '9'; i >= '0'; i--) {
			base64Alphabet[i] = (byte) (i - '0' + 52);
		}

		base64Alphabet['+'] = 62;
		base64Alphabet['/'] = 63;

		for (int i = 0; i <= 25; i++)
			lookUpBase64Alphabet[i] = (char) ('A' + i);

		for (int i = 26, j = 0; i <= 51; i++, j++)
			lookUpBase64Alphabet[i] = (char) ('a' + j);

		for (int i = 52, j = 0; i <= 61; i++, j++)
			lookUpBase64Alphabet[i] = (char) ('0' + j);
		lookUpBase64Alphabet[62] = '+';
		lookUpBase64Alphabet[63] = '/';

	}

	/**
	 * Encode a byte array and fold lines at the standard 76th character.
	 * 
	 * @param binaryData
	 *            <code>byte[]<code> to be base64 encoded
	 * @return the <code>String<code> with encoded data
	 */
	public static String encode(byte[] binaryData) {
		return encode(binaryData, BASE64DEFAULTLENGTH);
	}

	public static String encode(byte[] binaryData, int length) {

		if (length < 4) {
			length = Integer.MAX_VALUE;
		}

		if (binaryData == null)
			return null;

		int lengthDataBits = binaryData.length * EIGHTBIT;
		if (lengthDataBits == 0) {
			return "";
		}

		int fewerThan24bits = lengthDataBits % TWENTYFOURBITGROUP;
		int numberTriplets = lengthDataBits / TWENTYFOURBITGROUP;
		int numberQuartet = fewerThan24bits != 0 ? numberTriplets + 1
				: numberTriplets;
		int quartesPerLine = length / 4;
		int numberLines = (numberQuartet - 1) / quartesPerLine;
		char encodedData[] = null;

		encodedData = new char[numberQuartet * 4 + numberLines];

		byte k = 0, l = 0, b1 = 0, b2 = 0, b3 = 0;

		int encodedIndex = 0;
		int dataIndex = 0;
		int i = 0;
		if (fDebug) {
			System.out.println("number of triplets = " + numberTriplets);
		}

		for (int line = 0; line < numberLines; line++) {
			for (int quartet = 0; quartet < 19; quartet++) {
				b1 = binaryData[dataIndex++];
				b2 = binaryData[dataIndex++];
				b3 = binaryData[dataIndex++];

				if (fDebug) {
					System.out.println("b1= " + b1 + ", b2= " + b2 + ", b3= "
							+ b3);
				}

				l = (byte) (b2 & 0x0f);
				k = (byte) (b1 & 0x03);

				byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)
						: (byte) ((b1) >> 2 ^ 0xc0);

				byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4)
						: (byte) ((b2) >> 4 ^ 0xf0);
				byte val3 = ((b3 & SIGN) == 0) ? (byte) (b3 >> 6)
						: (byte) ((b3) >> 6 ^ 0xfc);

				if (fDebug) {
					System.out.println("val2 = " + val2);
					System.out.println("k4   = " + (k << 4));
					System.out.println("vak  = " + (val2 | (k << 4)));
				}

				encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
				encodedData[encodedIndex++] = lookUpBase64Alphabet[val2
						| (k << 4)];
				encodedData[encodedIndex++] = lookUpBase64Alphabet[(l << 2)
						| val3];
				encodedData[encodedIndex++] = lookUpBase64Alphabet[b3 & 0x3f];

				i++;
			}
			encodedData[encodedIndex++] = 0xa;
		}

		for (; i < numberTriplets; i++) {
			b1 = binaryData[dataIndex++];
			b2 = binaryData[dataIndex++];
			b3 = binaryData[dataIndex++];

			if (fDebug) {
				System.out.println("b1= " + b1 + ", b2= " + b2 + ", b3= " + b3);
			}

			l = (byte) (b2 & 0x0f);
			k = (byte) (b1 & 0x03);

			byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)
					: (byte) ((b1) >> 2 ^ 0xc0);

			byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4)
					: (byte) ((b2) >> 4 ^ 0xf0);
			byte val3 = ((b3 & SIGN) == 0) ? (byte) (b3 >> 6)
					: (byte) ((b3) >> 6 ^ 0xfc);

			if (fDebug) {
				System.out.println("val2 = " + val2);
				System.out.println("k4   = " + (k << 4));
				System.out.println("vak  = " + (val2 | (k << 4)));
			}

			encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
			encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | (k << 4)];
			encodedData[encodedIndex++] = lookUpBase64Alphabet[(l << 2) | val3];
			encodedData[encodedIndex++] = lookUpBase64Alphabet[b3 & 0x3f];
		}

		// form integral number of 6-bit groups
		if (fewerThan24bits == EIGHTBIT) {
			b1 = binaryData[dataIndex];
			k = (byte) (b1 & 0x03);
			if (fDebug) {
				System.out.println("b1=" + b1);
				System.out.println("b1<<2 = " + (b1 >> 2));
			}
			byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)
					: (byte) ((b1) >> 2 ^ 0xc0);
			encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
			encodedData[encodedIndex++] = lookUpBase64Alphabet[k << 4];
			encodedData[encodedIndex++] = PAD;
			encodedData[encodedIndex++] = PAD;
		} else if (fewerThan24bits == SIXTEENBIT) {
			b1 = binaryData[dataIndex];
			b2 = binaryData[dataIndex + 1];
			l = (byte) (b2 & 0x0f);
			k = (byte) (b1 & 0x03);

			byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)
					: (byte) ((b1) >> 2 ^ 0xc0);
			byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4)
					: (byte) ((b2) >> 4 ^ 0xf0);

			encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
			encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | (k << 4)];
			encodedData[encodedIndex++] = lookUpBase64Alphabet[l << 2];
			encodedData[encodedIndex++] = PAD;
		}

		// encodedData[encodedIndex] = 0xa;

		return new String(encodedData);
	}

	/**
	 * Decodes Base64 data into octects
	 * 
	 * @param encoded
	 *            Byte array containing Base64 data
	 * @return Array containind decoded data.
	 * @throws Exception
	 */
	public final static byte[] decode(String encoded)
			throws Exception {

		if (encoded == null)
			return null;

		return decodeInternal(encoded.getBytes());
	}

	protected final static byte[] decodeInternal(byte[] base64Data)
			throws Exception {
		// remove white spaces
		int len = removeWhiteSpace(base64Data);

		if (len % FOURBYTE != 0) {
			throw new Exception("decoding.divisible.four");
			// should be divisible by four
		}

		int numberQuadruple = (len / FOURBYTE);

		if (numberQuadruple == 0)
			return new byte[0];

		byte decodedData[] = null;
		byte b1 = 0, b2 = 0, b3 = 0, b4 = 0;

		int i = 0;
		int encodedIndex = 0;
		int dataIndex = 0;

		// decodedData = new byte[ (numberQuadruple)*3];
		dataIndex = (numberQuadruple - 1) * 4;
		encodedIndex = (numberQuadruple - 1) * 3;
		// first last bits.
		b1 = base64Alphabet[base64Data[dataIndex++]];
		b2 = base64Alphabet[base64Data[dataIndex++]];
		if ((b1 == -1) || (b2 == -1)) {
			throw new Exception("decoding.general");// if found
																	// "no data"
																	// just
																	// return
																	// null
		}

		byte d3, d4;
		b3 = base64Alphabet[d3 = base64Data[dataIndex++]];
		b4 = base64Alphabet[d4 = base64Data[dataIndex++]];
		if ((b3 == -1) || (b4 == -1)) {
			// Check if they are PAD characters
			if (isPad(d3) && isPad(d4)) { // Two PAD e.g. 3c[Pad][Pad]
				if ((b2 & 0xf) != 0)// last 4 bits should be zero
					throw new Exception("decoding.general");
				decodedData = new byte[encodedIndex + 1];
				decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
			} else if (!isPad(d3) && isPad(d4)) { // One PAD e.g. 3cQ[Pad]
				if ((b3 & 0x3) != 0)// last 2 bits should be zero
					throw new Exception("decoding.general");
				decodedData = new byte[encodedIndex + 2];
				decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
				decodedData[encodedIndex] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
			} else {
				throw new Exception("decoding.general");// an
																		// error
																		// like
																		// "3c[Pad]r",
																		// "3cdX",
																		// "3cXd",
																		// "3cXX"
																		// where
																		// X is
																		// non
																		// data
			}
		} else {
			// No PAD e.g 3cQl
			decodedData = new byte[encodedIndex + 3];
			decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
			decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
			decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
		}
		encodedIndex = 0;
		dataIndex = 0;
		// the begin
		for (i = numberQuadruple - 1; i > 0; i--) {
			b1 = base64Alphabet[base64Data[dataIndex++]];
			b2 = base64Alphabet[base64Data[dataIndex++]];
			b3 = base64Alphabet[base64Data[dataIndex++]];
			b4 = base64Alphabet[base64Data[dataIndex++]];

			if ((b1 == -1) || (b2 == -1) || (b3 == -1) || (b4 == -1)) {
				throw new Exception("decoding.general");// if
																		// found
																		// "no data"
																		// just
																		// return
																		// null
			}

			decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
			decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
			decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
		}
		return decodedData;
	}

	/**
	 * remove WhiteSpace from MIME containing encoded Base64 data.
	 * 
	 * @param data
	 *            the byte array of base64 data (with WS)
	 * @return the new length
	 */
	protected static int removeWhiteSpace(byte[] data) {
		if (data == null)
			return 0;

		// count characters that's not whitespace
		int newSize = 0;
		int len = data.length;
		for (int i = 0; i < len; i++) {
			byte dataS = data[i];
			if (!isWhiteSpace(dataS))
				data[newSize++] = dataS;
		}
		return newSize;
	}

	protected static final boolean isWhiteSpace(byte octect) {
		return (octect == 0x20 || octect == 0xd || octect == 0xa || octect == 0x9);
	}
	
	   protected static final boolean isPad(byte octect) {
	       return (octect == PAD);
	   }
	   
	   
	   public static String getBASE64(String s)
	    {
	        if (s == null)
	            return null;
	        try
	        {
	            return (new sun.misc.BASE64Encoder()).encode(s.getBytes("UTF-8"));
	        } catch (UnsupportedEncodingException e)
	        {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        return null;
	    }

	    public static String getBASE64(byte[] b)
	    {
	        return (new sun.misc.BASE64Encoder()).encode(b);
	    }

	    // BASE64 编码的字符串 s 进行解码
	    public static String getFromBASE64(String s)
	    {
	        if (s == null)
	            return null;
	        BASE64Decoder decoder = new BASE64Decoder();
	        try
	        {
	            byte[] b = decoder.decodeBuffer(s);
	            return new String(b, "UTF-8");
	        } catch (Exception e)
	        {
	            return null;
	        }
	    }

	    // �?BASE64 编码的字符串 s 进行解码
	    public static byte[] getBytesBASE64(String s)
	    {
	        if (s == null)
	            return null;
	        BASE64Decoder decoder = new BASE64Decoder();
	        try
	        {
	            byte[] b = decoder.decodeBuffer(s);
	            return b;
	        } catch (Exception e)
	        {
	            return null;
	        }
	    } 
}
