// NOTICE 2020-04-18
// Please see the comments below about why this is not a great PRNG.

// Read summary by @bryc here:
// https://github.com/bryc/code/blob/master/jshash/PRNGs.md

// Have a look at js-arbit which uses Alea:
// https://github.com/blixt/js-arbit

/**
 * Creates a pseudo-random value generator. The seed must be an integer.
 *
 * Uses an optimized version of the Park-Miller PRNG.
 * http://www.firstpr.com.au/dsp/rand31/
 */
export default function RNG(seed: number) {
  let _seed = seed % 2147483647;
  if (_seed <= 0) _seed += 2147483646;

  const next = function () {
    return (_seed = (_seed * 16807) % 2147483647);
  };

  /**
   * Returns a pseudo-random floating point number in range [0, 1).
   */
  const nextFloat = function () {
    // We know that result of next() will be 1 to 2147483646 (inclusive).
    return (next() - 1) / 2147483646;
  };

  return nextFloat;
}
