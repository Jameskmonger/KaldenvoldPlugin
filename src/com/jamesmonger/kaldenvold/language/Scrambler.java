package com.jamesmonger.kaldenvold.language;

import java.util.Random;

public class Scrambler
{
	enum CharacterTypes
	{
		UPPERCASE, LOWERCASE, NUMBER
	}

	static int maxUnderstanding = 700;

	static String upperCaseAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static String lowerCaseAlphabet = "abcdefghijklmnopqrstuvwxyz";
	static String numbers = "0123456789";

	static char getRandomCharacter(CharacterTypes type)
	{
		Random r = new Random();
		switch (type)
		{
			case UPPERCASE:
				return upperCaseAlphabet.charAt(r.nextInt(upperCaseAlphabet
						.length()));
			case LOWERCASE:
				return lowerCaseAlphabet.charAt(r.nextInt(lowerCaseAlphabet
						.length()));
			case NUMBER:
				return numbers.charAt(r.nextInt(numbers.length()));
			default:
				return lowerCaseAlphabet.charAt(r.nextInt(lowerCaseAlphabet
						.length()));
		}
	}

	public static String scrambleString(String string, int understanding)
	{
		if (understanding >= maxUnderstanding)
			return string;

		Random r = new Random();
		StringBuilder scrambledString = new StringBuilder();
		for (int i = 0; i < string.length(); i++)
		{
			char letter = string.charAt(i);
			if (understanding > 500)
			{
				if (r.nextInt(11) >= 3)
				{
					scrambledString.append(letter);
					continue;
				}
			}
			if (understanding > 400)
			{
				if (r.nextInt(11) >= 5)
				{
					scrambledString.append(letter);
					continue;
				}
			}
			if (understanding > 300)
			{
				if (r.nextInt(11) >= 7)
				{
					scrambledString.append(letter);
					continue;
				}
			}
			if (understanding > 100)
			{
				if (r.nextInt(11) >= 9)
				{
					scrambledString.append(letter);
					continue;
				}
			}

			if (Character.isUpperCase(letter))
			{
				scrambledString
						.append(getRandomCharacter(CharacterTypes.UPPERCASE));
				continue;
			}
			if (Character.isLowerCase(letter))
			{
				scrambledString
						.append(getRandomCharacter(CharacterTypes.LOWERCASE));
				continue;
			}
			if (Character.isDigit(letter))
			{
				scrambledString
						.append(getRandomCharacter(CharacterTypes.NUMBER));
				continue;
			}
			scrambledString.append(letter);
		}
		return scrambledString.toString();
	}
}