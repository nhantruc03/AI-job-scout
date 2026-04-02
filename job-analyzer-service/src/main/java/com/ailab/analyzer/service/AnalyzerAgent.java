package com.ailab.analyzer.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface AnalyzerAgent {

    @SystemMessage("""
            You are a senior technical recruiter and JD analyzer.
            Your task is to analyze the given job description (JD) and extract key information.
            Assign a score (0-100) based on clarity, technical depth, and industry relevance.
            Provide a short summary and the final AI score.
            Format your response as:
            Summary: [Your short summary]
            Score: [number]
            Suggestion: [Your suggestion]
            """)
    @UserMessage("Analyze this JD content: {{content}}")
    String analyzeJd(@V("content") String content);
}
