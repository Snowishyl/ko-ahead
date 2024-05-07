import React, { useState, useRef, useEffect } from 'react';
import './App.css';
// import { SSE } from 'sse.js';

function App() {
  const [prompt, setPrompt] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [result, setResult] = useState('');
  const resultRef = useRef<string>(result);
  useEffect(() => {
    resultRef.current = result;
  }, [result]);
  const handlePromptChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    const inputValue = e.target.value;
    setPrompt(inputValue);
  };

  const handleClearBtnClicked = () => {
    setPrompt('');
    setResult('');
  };

  const handleSubmitPromptBtnClicked = async () => {
    if (prompt !== '') {
      setIsLoading(true);
      setResult(prompt);
      const url = `http://localhost:9005/v1/chat/completions`;
      const data = {
        model: 'llama2',
        messages: [
          {
            role: 'user',
            content: prompt,
          },
        ],
        temperature: 0.75,
        top_p: 0.95,
        max_tokens: 256,
        stream: false,
        n: 1,
      };

      try {
        const response = await fetch(url, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(data),
        });

        if (response.ok) {
          const responseData = await response.json();
          const text = responseData.choices[0].message.content;
          setResult(text);
        } else {
          console.error('Request failed with status:', response.status);
        }
      } catch (error) {
        console.error('Request failed with error:', error);
      } finally {
        setIsLoading(false);
      }
    }
  };

  return (
    <div className="app-container">
      <div className="app-card">
        <h1 className="app-heading">Ko-ahead</h1>
        <p className="app-description">
          A demo developed in Kotlin for showcasing
        </p>
        <p>
          the Llama2 http inference service
        </p>
        <textarea
          value={prompt}
          onChange={handlePromptChange}
          placeholder="Insert your prompt here ..."
          className="app-textarea"
        />
        <div className="button-container">
          <button
            type="button"
            className="app-button"
            onClick={handleSubmitPromptBtnClicked}
            disabled={isLoading}
          >
            {isLoading ? 'Loading...' : 'Submit Prompt'}
          </button>
          <button
            type="button"
            className="app-button"
            onClick={handleClearBtnClicked}
          >
            Clear
          </button>
        </div>
        {result !== '' && (
          <div className="app-result">
            <h5 className="result-heading">Result:</h5>
            <p className="result-text">{result}</p>
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
