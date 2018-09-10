#include <stdio.h>
#include <stdint.h>         
#include <wmmintrin.h>  //AES-NI intrinsics library




static __m128i key_expansion(__m128i key,__m128i generated_key){

    generated_key = _mm_shuffle_epi32(generated_key, _MM_SHUFFLE(3,3,3,3));
    key = _mm_xor_si128(key, _mm_slli_si128(key, 4));
    key = _mm_xor_si128(key, _mm_slli_si128(key, 4));
    key = _mm_xor_si128(key, _mm_slli_si128(key, 4));
    return _mm_xor_si128(key, generated_key);
}





int main(void){

    uint8_t plainText[]	= {0x32, 0x43, 0xf6, 0xa8, 0x88, 0x5a, 0x30, 0x8d, 0x31, 0x31, 0x98, 0xa2, 0xe0, 0x37, 0x07, 0x34};
    uint8_t enc_key[]	= {0x2b, 0x7e, 0x15, 0x16, 0x28, 0xae, 0xd2, 0xa6, 0xab, 0xf7, 0x15, 0x88, 0x09, 0xcf, 0x4f, 0x3c};
    uint8_t cipher[]	= {0x39, 0x25, 0x84, 0x1d, 0x02, 0xdc, 0x09, 0xfb, 0xdc, 0x11, 0x85, 0x97, 0x19, 0x6a, 0x0b, 0x32};
    uint8_t computed_cipher[16];



    
    /*
    *	Key Scheduling is being performed here
    *	Expansion of given cipher key into 11 128 bit partial keys
    *	used in one initial round, 9 main rounds and one final round
    */

    __m128i key_schedule[11];
 
    key_schedule[0] = _mm_loadu_si128((const __m128i*) enc_key);
    key_schedule[1]  = key_expansion(key_schedule[0], _mm_aeskeygenassist_si128(key_schedule[0], 0x01));
    key_schedule[2]  = key_expansion(key_schedule[1], _mm_aeskeygenassist_si128(key_schedule[1], 0x02));
    key_schedule[3]  = key_expansion(key_schedule[2], _mm_aeskeygenassist_si128(key_schedule[2], 0x04));
    key_schedule[4]  = key_expansion(key_schedule[3], _mm_aeskeygenassist_si128(key_schedule[3], 0x08));
    key_schedule[5]  = key_expansion(key_schedule[4], _mm_aeskeygenassist_si128(key_schedule[4], 0x10));
    key_schedule[6]  = key_expansion(key_schedule[5], _mm_aeskeygenassist_si128(key_schedule[5], 0x20));
    key_schedule[7]  = key_expansion(key_schedule[6], _mm_aeskeygenassist_si128(key_schedule[6], 0x40));
    key_schedule[8]  = key_expansion(key_schedule[7], _mm_aeskeygenassist_si128(key_schedule[7], 0x80));
    key_schedule[9]  = key_expansion(key_schedule[8], _mm_aeskeygenassist_si128(key_schedule[8], 0x1B));
    key_schedule[10] = key_expansion(key_schedule[9], _mm_aeskeygenassist_si128(key_schedule[9], 0x36));




    /*  Encyption process starts here */
    

    __m128i state_array = _mm_loadu_si128((__m128i *) plainText);


    /* 	Initial round */

	state_array = _mm_xor_si128(state_array, key_schedule[ 0]); 

	/* 	
	*	9 main rounds
	*	each round consists of 4 operations: SubBytes, ShiftRows, MixColumns, AddRoundKeys
	*	using _mm_aesenc_si128 we are performing these 4 operations in one instruction 
	*/

	for (int i = 1; i < 10; ++i){

		state_array = _mm_aesenc_si128(state_array, key_schedule[i]);
	}

	/* 
	*	Final round consists of 3 operations: SubBytes, ShiftRows, AddRoundKeys
	*	using _mm_aesenclast_si128 we are performing these 3 operations in one instruction 
	*/

    state_array = _mm_aesenclast_si128(state_array, key_schedule[10]);

    _mm_storeu_si128((__m128i *) computed_cipher, state_array);

    	



    //print

    uint8_t *val =  cipher;
    printf("cipher defined: %i %i %i %i %i %i %i %i \n", 
           val[0], val[1], val[2], val[3], val[4], val[5], 
           val[6], val[7]);


    uint8_t *cval =  computed_cipher;
    printf("computed_cipher : %i %i %i %i %i %i %i %i \n", 
           cval[0], cval[1], cval[2], cval[3], cval[4], cval[5], 
           cval[6], cval[7]);


    return 0;
}