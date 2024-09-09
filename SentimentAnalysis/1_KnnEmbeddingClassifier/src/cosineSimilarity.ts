// Criado por Lucas Bortoli em Abril 2024.

function dotProduct(vec1: Float32Array, vec2: Float32Array): number {
  let dotProduct = 0;
  for (let i = 0; i < vec1.length; i++) {
    dotProduct += vec1[i] * vec2[i];
  }
  return dotProduct;
}

function magnitude(vec: Float32Array): number {
  let sum = 0;
  for (let i = 0; i < vec.length; i++) {
    sum += vec[i] * vec[i];
  }
  return Math.sqrt(sum);
}

export function bufferToFloat32Array(buffer: Buffer): Float32Array {
  if (buffer.length % 4 !== 0) {
    throw new Error("Buffer length must be a multiple of 4");
  }

  const float32Array = new Float32Array(buffer.length / 4);
  for (let i = 0; i < float32Array.length; i++) {
    float32Array[i] = buffer.readFloatLE(i * 4);
  }
  return float32Array;
}

export function cosineSimilarity(vec1: Float32Array, vec2: Float32Array): number {
  if (vec1.length !== vec2.length) {
    throw new Error("Vector dimensions must be the same");
  }

  const dot = dotProduct(vec1, vec2);
  const mag1 = magnitude(vec1);
  const mag2 = magnitude(vec2);

  // Prevent division by zero
  if (mag1 === 0 || mag2 === 0) {
    return 0;
  }

  return dot / (mag1 * mag2);
}
