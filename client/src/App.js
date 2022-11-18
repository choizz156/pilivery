import styled from 'styled-components';
import GlobalStyle from './assets/style/GlobalStyle';
import Tab from './components/Tabs/Tab';

function App() {
	return (
		<>
			<GlobalStyle />

			<Box>
				<Tab />
			</Box>
		</>
	);
}

export default App;

const Box = styled.div`
	background-color: var(--gray-200);
	width: 50vw;
	height: 50vh;
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	// 컨텐츠 사이 세로 간격 조절
	& > * + * {
		margin-top: 0.5rem;
	}
`;
