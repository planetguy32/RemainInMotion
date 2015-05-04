package me.planetguy.lib.include.it.unimi.dsi.util;

import java.io.Serializable;
import java.util.Random;

/*		 
 * DSI utilities
 *
 * Copyright (C) 2013-2015 Sebastiano Vigna 
 *
 *  This library is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation; either version 3 of the License, or (at your option)
 *  any later version.
 *
 *  This library is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 *  for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses/>.
 *
 */


/** A fast, top-quality {@linkplain Random pseudorandom number generator} that
 * combines a long-period instance of George Marsaglia's Xorshift generators (described in <a
 * href="http://www.jstatsoft.org/v08/i14/paper/">&ldquo;Xorshift RNGs&rdquo;</a>, <i>Journal of
 * Statistical Software</i>, 8:1&minus;6, 2003) with a multiplication.
 * 
 * <p><strong>Warning</strong>: in version 2.2.3 the seeding procedure has been changed, as
 * it now uses a {@link SplitMix64RandomGenerator}.
 *  
 * <p>More details can be found on the <a href="http://xorshift.di.unimi.it/"><code>xorshift*</code>/<code>xorshift+</code> generators and the PRNG shootout</a> page.
 *
 * <p>Note that this is <strong>not</strong> a cryptographic-strength pseudorandom number generator. Its period is
 * 2<sup>1024</sup>&nbsp;&minus;&nbsp;1, which is more than enough for any massive parallel application (it is actually
 * possible to define analogously a generator with period 2<sup>4096</sup>&nbsp;&minus;&nbsp;1,
 * but its interest is eminently academic). 
 * 
 * @see it.unimi.dsi.util
 * @see Random
 * @see XorShift1024StarRandomGenerator
 */
public class XorShift implements Serializable{
	
	static{
		Random r=new Random();
		genericPRNG=new XorShift(r.nextLong(), r.nextLong(), r.nextLong());
	}
	
	/** PRNG to use for non-chunk-based generation */
	public static XorShift genericPRNG;
	

	/** 2<sup>-53</sup>. */
	private static final double NORM_53 = 1. / ( 1L << 53 );
	/** 2<sup>-24</sup>. */
	private static final double NORM_24 = 1. / ( 1L << 24 );

	/** The internal state of the algorithm. */
	private long[] s=new long[16];
	private int p=0;
	
	/** Creates a new generator seeded based on chunk position and world seed */
	public XorShift(long chunkX, long chunkZ, long worldSeed) {
		setState(new long[]{
				//seed from position...
				chunkX, 
				chunkZ, 
				worldSeed, 
				//and random data...
				869904368556852052L,
				9200919385050488543L, 
				8648534474279014740L,
				1337612314582118464L,
				325600724316358864L,
				5369582247259230420L,
				4743040926333721602L,
				5095614266888937462L,
				209034732904266203L,
				9097333966875356173L,
				8546065844578658660L,
				4328080056916821297L,
				9105802618821670525L
		}, 0);
	}

	protected int next( int bits ) {
		return (int)( nextLong() >>> 64 - bits );
	}
	
	public long nextLong() {
		final long s0 = s[ p ];
		long s1 = s[ p = ( p + 1 ) & 15 ];
		s1 ^= s1 << 31;
		return ( s[ p ] = s1 ^ s0 ^ ( s1 >>> 11 ) ^ ( s0 >>> 30 ) ) * 1181783497276652981L;
	}

	public int nextInt() {
		return (int)nextLong();
	}
	
	public int nextInt( final int n ) {
		return (int)nextLong( n );
	}
	
	/** Returns a pseudorandom uniformly distributed {@code long} value
     * between 0 (inclusive) and the specified value (exclusive), drawn from
     * this random number generator's sequence. The algorithm used to generate
     * the value guarantees that the result is uniform, provided that the
     * sequence of 64-bit values produced by this generator is. 
     * 
     * @param n the positive bound on the random number to be returned.
     * @return the next pseudorandom {@code long} value between {@code 0} (inclusive) and {@code n} (exclusive).
     */
	public long nextLong( final long n ) {
        if ( n <= 0 ) throw new IllegalArgumentException();
		// No special provision for n power of two: all our bits are good.
		for(;;) {
			final long bits = nextLong() >>> 1;
			final long value = bits % n;
			if ( bits - value + ( n - 1 ) >= 0 ) return value;
		}
	}
	
	 public double nextDouble() {
		return ( nextLong() >>> 11 ) * NORM_53;
	}
	
	public float nextFloat() {
		return (float)( ( nextLong() >>> 40 ) * NORM_24 );
	}

	public boolean nextBoolean() {
		return nextLong() < 0;
	}
	
	public void nextBytes( final byte[] bytes ) {
		int i = bytes.length, n = 0;
		while( i != 0 ) {
			n = Math.min( i, 8 );
			for ( long bits = nextLong(); n-- != 0; bits >>= 8 ) bytes[ --i ] = (byte)bits;
		}
	}

	/** Sets the state of this generator.
	 * 
	 * <p>The internal state of the generator will be reset, and the state array filled with the provided array.
	 * 
	 * @param state an array of 16 longs; at least one must be nonzero.
	 * @param p the internal index. 
	 */
	public void setState( final long[] state, final int p ) {
		if ( state.length != s.length ) 
			throw new IllegalArgumentException( "The argument array contains " + state.length + " longs instead of " + s.length );
		System.arraycopy( state, 0, s, 0, s.length );
		this.p = p;
	}
}
