package com.android.quiz.modelo;

public class Utility {
	
	/**
	 * Method to capitalise the first letter of a given string
	 * 
	 * @param s
	 * @return
	 */
	public static String capitalise(String s){
		if (s==null || s.length()==0) return s;
		
		String s1 = s.substring(0, 1).toUpperCase() + s.substring(1);
		return s1;
	}

	/**
	 * Method to get set of answers for a list of questions
	 * @param questions
	 * @return
	 */
	/*public static String getAnswers(List<Questao> questions) {
		int question = 1;
		StringBuffer sb = new StringBuffer();
		
		for (Questao q : questions){
			sb.append("Q").append(question).append(") ").append(q.getQuestao()).append("? \n");
			sb.append("Answer: ").append(q.getResposta()).append("\n\n");
			question ++;
		}
		
		return sb.toString();
	}*/

}
