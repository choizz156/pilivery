import styled, { css } from 'styled-components';
import PayPageContainer from './PayPageContainer';
// 해당 컴포넌트는 외부에서 상품 정복 혹은 가능하면 가격 정보만 받아서 뿌려주어야 함. 지금이야 급해서 그냥 하드코딩 해 놨음
export default function FinalPaymentInformation() {
	return (
		<PayInfoContainer>
			<PayInfoHeading>최종 결제 정보</PayInfoHeading>
			<FinalPayInformation>
				<TotalBox>
					<TotalName>상품합계</TotalName>
					<TotalPrice>120,000 원</TotalPrice>
				</TotalBox>
				<TotalBox>
					<TotalName>할인합계</TotalName>
					<TotalPrice discounted> - 120,000 원</TotalPrice>
				</TotalBox>
				<TotalBox>
					<TotalName>최종결제금액</TotalName>
					<TotalPrice total>120,000 원</TotalPrice>
				</TotalBox>
			</FinalPayInformation>
		</PayInfoContainer>
	);
}

const PayInfoContainer = styled.section`
	width: 588px;
	background-color: white;
	border-radius: 10px;
	box-shadow: 0px 1px 4px rgba(0, 0, 0, 0.07);
	padding: 50px 54px;
`;

const PayInfoHeading = styled.h2`
	font-size: 20px;
	color: var(--gray-500);
	margin-bottom: 44px;
`;

const FinalPayInformation = styled.article`
	display: flex;
	flex-direction: column;
	justify-content: space-between;
	width: 480px;
	height: 75px;
`;

const TotalBox = styled.div`
	width: 100%;
	display: flex;
	flex-direction: row;
	justify-content: space-between;
`;

const TotalName = styled.div`
	font-size: 16px;
	color: var(--gray-400);
`;

const TotalPrice = styled.div`
	font-size: 16px;
	color: var(--gray-500);
	${(props) =>
		props.discounted &&
		css`
			color: var(--purple-200);
		`}
	${(props) =>
		props.total &&
		css`
			font-weight: var(--extraBold);
		`}
`;
