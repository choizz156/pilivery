import styled from 'styled-components';

function Footer() {
	return (
		<FooterContainer>
			<Wrap>
				<Top>
					<Title
						as="a"
						href="https://github.com/codestates-seb/seb40_main_033"
						target="_blank"
						rel="noopener noreferrer"
					>
						서비스 소개
					</Title>
					<Title
						as="a"
						href="https://github.com/codestates-seb/seb40_main_033"
						target="_blank"
						rel="noopener noreferrer"
					>
						GitHub
					</Title>
					<Title
						as="a"
						href="https://github.com/codestates-seb/seb40_main_033"
						target="_blank"
						rel="noopener noreferrer"
					>
						Figma
					</Title>
				</Top>
				<Body>
					<BoxLeft>
						<Text>서울특별시 강남구 영동대로 106길 12, 502호</Text>
						<Text>
							사업자 번호: 001-717-99-09821 , 통신판매업신고번호: 2020-서울강남
						</Text>
						<Text>고객센터: 1122-1234</Text>

						<Box>
							<Contents
								as="a"
								href="https://github.com/dohyeons"
								target="_blank"
								rel="noopener noreferrer"
							>
								도현수
							</Contents>
							<Contents
								as="a"
								href="https://github.com/uxolrv"
								target="_blank"
								rel="noopener noreferrer"
							>
								김세연
							</Contents>
							<Contents
								as="a"
								href="https://github.com/kihyeoon"
								target="_blank"
								rel="noopener noreferrer"
							>
								방기현
							</Contents>
							<Contents
								as="a"
								href="https://github.com/jihwanAn"
								target="_blank"
								rel="noopener noreferrer"
							>
								안지환
							</Contents>
							<Contents
								as="a"
								href="https://github.com/zirryo"
								target="_blank"
								rel="noopener noreferrer"
							>
								김지효
							</Contents>
							<Contents
								as="a"
								href="https://github.com/jisoo27"
								target="_blank"
								rel="noopener noreferrer"
							>
								김지수
							</Contents>
							<Contents
								as="a"
								href="https://github.com/choizz156"
								target="_blank"
								rel="noopener noreferrer"
							>
								최민석
							</Contents>
						</Box>
						<Copyright>© Copyright ⓒ 2022 pillivery</Copyright>
					</BoxLeft>
					<BoxRight>
						<Img />
						<Pil>pillivery</Pil>
					</BoxRight>
				</Body>
			</Wrap>
		</FooterContainer>
	);
}

const FooterContainer = styled.footer`
	background-color: var(--gray-500);
	width: 100%;
	height: 365px;
	display: flex;
	justify-content: center;
	align-items: center;
`;

const Wrap = styled.div`
	width: 1100px;
	height: 290px;
	display: grid;
`;

const Top = styled.div`
	margin-top: 5px;
	display: flex;
`;

const Title = styled.div`
	font-size: 16px;
	color: var(--gray-200);
	font-weight: var(--bold);
	margin-left: 90px;
`;

const Body = styled.div`
	border-top: 1px solid white;
	height: 220px;
	display: flex;
	justify-content: space-between;
	padding-top: 60px;
`;

const Text = styled.div`
	color: var(--gray-200);
	font-weight: var(--regular);
	margin-bottom: 10px;
	margin-left: 30px;
`;

const BoxLeft = styled.div`
	flex-direction: column;
	display: flex;
	justify-content: center;
`;

const Box = styled.div`
	display: flex;
`;

const Contents = styled.div`
	color: var(--gray-200);
	font-weight: var(--regular);
	margin-top: 50px;
	margin-left: 40px;
`;

const BoxRight = styled.div`
	display: flex;
`;

const Img = styled.div`
	border: 1px solid var(--gray-200);
	width: 60px;
	height: 60px;
	margin-top: 30px;
	margin-right: 30px;
`;

const Pil = styled.div`
	color: var(--gray-200);
	font-weight: var(--bold);
	font-size: 18px;
	margin-top: 60px;
	margin-right: 240px;
`;

const Copyright = styled.div`
	color: var(--gray-200);
	font-weight: var(--light);
	font-size: 13px;
	margin-top: 30px;
	margin-left: 200px;
`;

export default Footer;
